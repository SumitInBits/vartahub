package com.sumitinbits.iam.vartahub.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final KeycloakConfig keycloakConfig;
    private final KeycloakConfigParam keycloakConfigParam;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        configureKeycloak();
    }


    private void configureKeycloak() {
        try (
                Keycloak kc = KeycloakBuilder.builder()
                        .serverUrl(keycloakConfig.getServerUrl())
                        .realm("master")
                        .clientId("admin-cli")
                        .username(keycloakConfigParam.getAdminUsername())
                        .password(keycloakConfigParam.getAdminPassword())
                        .build()
        ) {

            String realm = keycloakConfig.getRealm();
            boolean realmExists = kc.realms().findAll().stream().anyMatch(r -> r.getRealm().equals(realm));
            if (realmExists) {
                log.info("Keycloak Realm {} already exists. Skipping auto-initialization.", realm);
                return;
            }

            setupRealm(kc, realm);

            ClientRepresentation frontendClient = getFrontendClient();
            ClientRepresentation iamClient = getIamClient();
            kc.realm(realm).clients().create(frontendClient);
            kc.realm(realm).clients().create(iamClient);

            assignRealmAdminRole(
                    kc,
                    realm,
                    keycloakConfig.getClientId()
            );

            // Realm roles
            setupRealmRoles(kc, realm);

            log.info("Keycloak initialization completed");
        }
    }

    private  ClientRepresentation getFrontendClient() {
        ClientRepresentation uiClient = new ClientRepresentation();
        uiClient.setClientId("vartahub-ui");
        uiClient.setPublicClient(true);
        uiClient.setEnabled(true);
        // Authorization Code Flow
        uiClient.setStandardFlowEnabled(true);

        // Disable flows not needed by Angular SPA
        uiClient.setDirectAccessGrantsEnabled(false);
        uiClient.setImplicitFlowEnabled(false);
        uiClient.setServiceAccountsEnabled(false);

        // URLs
        uiClient.setRootUrl("http://localhost:4200");
        uiClient.setBaseUrl("http://localhost:4200");
        uiClient.setRedirectUris(
                List.of("http://localhost:4200/*")
        );
        uiClient.setWebOrigins(
                List.of("http://localhost:4200")
        );

        // Logout redirect
        uiClient.setAttributes(Map.of(
                "pkce.code.challenge.method", "S256",
                "post.logout.redirect.uris", "http://localhost:4200/*"
        ));

        return uiClient;
    }

    private ClientRepresentation getIamClient() {
        // 3. Create Backend Client (vartahub-iam-service) - Confidential Client
        ClientRepresentation iamClient = new ClientRepresentation();
        iamClient.setClientId(keycloakConfig.getClientId());
        // Confidential client
        iamClient.setPublicClient(false);
        iamClient.setClientAuthenticatorType("client-secret");
        iamClient.setSecret(keycloakConfigParam.getIamClientSecret());
        iamClient.setEnabled(true);
        // Service account for backend-to-Keycloak communication
        iamClient.setServiceAccountsEnabled(true);
        // Authorization Services not required
        iamClient.setAuthorizationServicesEnabled(false);
        // OAuth/OIDC browser login not required
        iamClient.setStandardFlowEnabled(false);
        iamClient.setDirectAccessGrantsEnabled(false);
        iamClient.setImplicitFlowEnabled(false);
        return iamClient;
    }

    private void assignRealmAdminRole(Keycloak kc, String realm, String clientId) {

        RealmResource realmResource = kc.realm(realm);

        // Get the backend client
        ClientRepresentation iamClient = realmResource
                .clients()
                .findByClientId(clientId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Client not found: " + clientId
                        )
                );

        // Built-in Keycloak client containing administration roles
        ClientRepresentation realmManagement = realmResource
                .clients()
                .findByClientId("realm-management")
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "realm-management client not found"
                        )
                );

        // Get the realm-admin role
        RoleRepresentation realmAdminRole = realmResource
                .clients()
                .get(realmManagement.getId())
                .roles()
                .get("realm-admin")
                .toRepresentation();

        // Get service account user
        UserRepresentation serviceAccountUser = realmResource
                .users()
                .search(
                        "service-account-" + clientId,
                        true
                )
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Service account not found for client: " + clientId
                        )
                );

        // Assign realm-admin to service account
        realmResource
                .users()
                .get(serviceAccountUser.getId())
                .roles()
                .clientLevel(realmManagement.getId())
                .add(List.of(realmAdminRole));
    }

    private void setupRealmRoles(Keycloak kc, String realm) {

        RealmResource realmResource = kc.realm(realm);

        // Create roles
        RoleRepresentation userRole = new RoleRepresentation();
        userRole.setName("user");
        userRole.setDescription("Basic user role");

        RoleRepresentation instructorRole = new RoleRepresentation();
        instructorRole.setName("instructor");
        instructorRole.setDescription("Instructor role");

        RoleRepresentation adminRole = new RoleRepresentation();
        adminRole.setName("admin");
        adminRole.setDescription("Administrator role");

        // Create realm roles
        realmResource.roles().create(userRole);
        realmResource.roles().create(instructorRole);
        realmResource.roles().create(adminRole);

        // instructor -> user
        realmResource.roles()
                .get("instructor")
                .addComposites(List.of(
                        realmResource.roles()
                                .get("user")
                                .toRepresentation()
                ));

        // admin -> instructor
        // Since instructor already contains user,
        // admin effectively contains both instructor and user.
        realmResource.roles()
                .get("admin")
                .addComposites(List.of(
                        realmResource.roles()
                                .get("instructor")
                                .toRepresentation()
                ));
    }

    private void setupRealm(Keycloak kc, String realm) {
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realm);
        realmRepresentation.setEnabled(true);

        kc.realms().create(realmRepresentation);

        RealmResource realmResource = kc.realm(realm);
        RealmRepresentation realmConfig = realmResource.toRepresentation();
        // Users can register themselves
        realmConfig.setRegistrationAllowed(true);
        // Use email as username during registration
        // Allow login using email address
        realmConfig.setLoginWithEmailAllowed(true);
        // Require users to verify their email address
        realmConfig.setVerifyEmail(true);
        // Allow "Forgot password?"
        realmConfig.setResetPasswordAllowed(true);
        // Allow "Remember me"
        realmConfig.setRememberMe(true);
        realmResource.update(realmConfig);


        /*
         * ============================================================
         * 2. Google Identity Provider
         * ============================================================
         */
        IdentityProviderRepresentation google = new IdentityProviderRepresentation();
        google.setAlias("google");
        google.setDisplayName("Google");
        google.setProviderId("google");
        google.setEnabled(true);

        /*
         * Trust the email claim returned by Google.
         *
         * This means Keycloak treats the Google-provided email
         * as already verified instead of asking the user to verify
         * the same email again.
         */
        google.setTrustEmail(true);
        Map<String, String> googleConfig = new HashMap<>();
        googleConfig.put("clientId", keycloakConfigParam.getGoogleProviderClientId());
        googleConfig.put("clientSecret", keycloakConfigParam.getGoogleProviderClientSecret());
        googleConfig.put("prompt", "select_account");
        /*
         * Use Google's OpenID Connect discovery/JWKS configuration.
         */
        google.setConfig(googleConfig);
        realmResource.identityProviders().create(google);


        /*
         * ============================================================
         * 3. SMTP / Email configuration
         * ============================================================
         *
         * Required for:
         *   - Verify email
         *   - Forgot password
         *   - Password reset
         *   - Other Keycloak email actions
         */
        Map<String, String> smtp = new HashMap<>();
        smtp.put("host", keycloakConfigParam.getSmtpHost());
        smtp.put("port", String.valueOf(keycloakConfigParam.getSmtpPort()));
        smtp.put("from", "admin@xorgrid.in");
        smtp.put("fromDisplayName", "XORGrid");
        smtp.put("auth", "true");
        smtp.put("user", keycloakConfigParam.getSmtpUser());
        smtp.put("password", keycloakConfigParam.getSmtpPassword());

        /*
         * TLS settings depend on your SMTP provider.
         *
         * For STARTTLS SMTP (commonly port 587):
         */
        smtp.put("starttls", "true");
        realmConfig = realmResource.toRepresentation();
        realmConfig.setSmtpServer(smtp);
        realmResource.update(realmConfig);
    }
}

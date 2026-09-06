package com.sumitinbits.vartahub.commons.proxy;

import com.sumitinbits.vartahub.commons.api.ApplicationProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@AutoConfiguration
public class ApplicationProxyConfig {
    private static final String HTTP_PROTOCOL = "http://";

    @Bean
    @LoadBalanced
    @Qualifier("loadBalancedRestClient")
    public RestClient.Builder loadBalancedRestClient() {
        return RestClient.builder();
    }

    @Bean
    @Primary
    public RestClient.Builder plainRestClient() {
        return RestClient.builder();
    }

    @Bean
    public ApplicationProxy applicationProxy(
            @Qualifier("loadBalancedRestClient") RestClient.Builder loadBalancedRestClient,
            RestClient.Builder plainRestClient

    ) {

        return new ApplicationProxy() {

            @Override
            public <T> T getApplicationClient(String applicationName, Class<T> serviceInterface) {
                String baseURL = applicationName.contains(HTTP_PROTOCOL) ? applicationName : HTTP_PROTOCOL + applicationName;
                RestClient client = loadBalancedRestClient.baseUrl(baseURL).build();

                return HttpServiceProxyFactory
                        .builderFor(RestClientAdapter.create(client))
                        .build()
                        .createClient(serviceInterface);
            }

            @Override
            public <T> T getApplicationClientNoLoadBalance(String serviceURL, Class<T> serviceInterface) {
                if(!serviceURL.contains(HTTP_PROTOCOL)) {
                    throw new IllegalArgumentException("Service URL is not valid " + serviceURL);
                }

                RestClient client = plainRestClient.baseUrl(serviceURL).build();

                return HttpServiceProxyFactory
                        .builderFor(RestClientAdapter.create(client))
                        .build()
                        .createClient(serviceInterface);
            }
        };
    }
}

package com.sumitinbits.vartahub.gateway;

import com.sumitinbits.vartahub.gateway.config.VartahubGatewaySecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(VartahubGatewaySecurityConfig.class)
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

}

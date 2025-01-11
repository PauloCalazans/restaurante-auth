package com.calazanstec.restaurante.auth.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author Paulo Calazans on 10/01/2025
 */

@Validated
@Component
@ConfigurationProperties("restaurante.jwt.keystore")
@Setter
@Getter
public class JwtKeyStoreProperties {

    private String path;
    private String password;
    private String keypairAlias;
}

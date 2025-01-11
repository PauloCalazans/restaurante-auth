package com.calazanstec.restaurante.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

/**
 * @author Paulo Calazans on 04/01/2025
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtKeyStoreProperties jwtKeyStoreProperties;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                    .withClient("restaurante-web")
                    .secret(passwordEncoder.encode("web123"))
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("write", "read")
                .accessTokenValiditySeconds(60 * 60 * 6) //6 Horas o padrão é 12 horas
                .refreshTokenValiditySeconds(60 * 60 * 24 * 10) //10 dias o padrão e 30 dias

                .and()
                    .withClient("analytics")
                    .secret(passwordEncoder.encode("restaurante123"))
                    .authorizedGrantTypes("authorization_code")
                    .scopes("write", "read")
                    .redirectUris("http://aplicacao-client")

                .and()
                    .withClient("webadmin")
                    .authorizedGrantTypes("implicit")
                    .scopes("write", "read")
                    .redirectUris("http://aplicacao-client")

                .and()
                    .withClient("faturamento")
                    .secret(passwordEncoder.encode("fat123"))
                    .authorizedGrantTypes("client_credentials")
                    .scopes("write", "read")

                .and()
                    .withClient("checktoken")
                    .secret(passwordEncoder.encode("check123"));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("permitAll()");
        security.checkTokenAccess("isAuthenticated()")
            .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenGranter(tokenGranter(endpoints));
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();
        var jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());

        var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, jwtKeyStoreProperties.getPassword().toCharArray());
        var keyPair = keyStoreKeyFactory.getKeyPair(jwtKeyStoreProperties.getKeypairAlias());

        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }
}

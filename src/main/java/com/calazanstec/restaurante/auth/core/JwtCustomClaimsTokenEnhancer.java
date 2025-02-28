package com.calazanstec.restaurante.auth.core;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

/**
 * @author Paulo Calazans on 11/01/2025
 */

public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken acessToken, OAuth2Authentication authentication) {
        if(authentication.getPrincipal() instanceof AuthUser) {
            var authUser = (AuthUser) authentication.getPrincipal();
            var info = new HashMap<String, Object>();
            info.put("full_name", authUser.getFullName());
            info.put("user_id", authUser.getUserId());

            var oAuth2AccessToken = (DefaultOAuth2AccessToken) acessToken;
            oAuth2AccessToken.setAdditionalInformation(info);
        }

        return acessToken;
    }
}

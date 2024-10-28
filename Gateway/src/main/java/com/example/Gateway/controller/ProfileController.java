package com.example.Gateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
    @GetMapping("/userinfo")
    public Map<String, Object> userInfo(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {

        Map<String, Object> attributesMap = new HashMap<>();
        attributesMap.put("id_token", oidcUser.getIdToken().getTokenValue());
        attributesMap.put("access_token", authorizedClient.getAccessToken().getTokenValue());
        attributesMap.put("client_name", authorizedClient.getClientRegistration().getClientId());
        attributesMap.put("user_attributes", oidcUser.getAttributes());

        return attributesMap;
    }
}

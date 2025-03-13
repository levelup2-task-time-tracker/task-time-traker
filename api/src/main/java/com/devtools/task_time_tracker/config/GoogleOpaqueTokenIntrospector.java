package com.devtools.task_time_tracker.config;

import com.devtools.task_time_tracker.dts.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;


public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;

    public GoogleOpaqueTokenIntrospector(WebClient userInfoClient){
        this.userInfoClient = userInfoClient;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        UserInfo userInfo = userInfoClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfo.sub());
        attributes.put("name", userInfo.name());
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.name(), attributes, null);
    }
}
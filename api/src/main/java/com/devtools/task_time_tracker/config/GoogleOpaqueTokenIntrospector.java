package com.devtools.task_time_tracker.config;

import com.devtools.task_time_tracker.dts.UserInfo;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.repository.ProjectMemberRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;


public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;


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

        assert userInfo != null;
        Optional<UserModel> userModelOptional = userRepository.findBySubject(userInfo.sub());
        UserModel user;
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (userModelOptional.isEmpty()) {
            user = new UserModel();
            user.setSubject(userInfo.sub());
            user.setName(userInfo.name());
            userRepository.save(user);

        }

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfo.sub());
        attributes.put("name", userInfo.name());

        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.name(), attributes, authorities);
    }
}
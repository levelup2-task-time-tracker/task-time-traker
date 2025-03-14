package com.devtools.task_time_tracker.config;

import com.devtools.task_time_tracker.dts.UserInfo;
import com.devtools.task_time_tracker.model.RoleModel;
import com.devtools.task_time_tracker.model.UserModel;
import com.devtools.task_time_tracker.model.UserRoleModel;
import com.devtools.task_time_tracker.repository.RoleRepository;
import com.devtools.task_time_tracker.repository.UserRepository;
import com.devtools.task_time_tracker.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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
            String roleName = "Developer";
            user = new UserModel();
            user.setSubject(userInfo.sub());
            userRepository.save(user);
            Optional<RoleModel> roleModelOptional = roleRepository.findByRoleName(roleName);

            if (roleModelOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            RoleModel role = roleModelOptional.get();

            UserRoleModel userRoleModel = new UserRoleModel();
            userRoleModel.setRole(role);
            userRoleModel.setUser(user);

            userRoleRepository.save(userRoleModel);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));

        } else {
            user = userModelOptional.get();
            List<UserRoleModel> userRoles = userRoleRepository.findByUser(user);

            if (userRoles.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no roles");
            }
            userRoles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName())));
        }

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfo.sub());
        attributes.put("name", userInfo.name());
        attributes.put("model", user);

        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.name(), attributes, authorities);
    }
}
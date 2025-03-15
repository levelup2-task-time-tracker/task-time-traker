package com.devtools.task_time_tracker_cli.service;

import com.devtools.task_time_tracker_cli.component.AuthToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class ApiService {
    public static final String url = "http://localhost:8080/";

    @Autowired
    private AuthToken token;

    public AuthToken getToken() {
        return token;
    }

    public boolean authenticate(){
        return !token.isAuthenticated();
    }

    public ResponseEntity<String> getRequest(String endPoint, Map<String, Object> params){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);


        StringBuilder urlBuilder = new StringBuilder(url + endPoint);

        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");

            params.forEach((key, value) -> {
                urlBuilder.append(key).append("=")
                        .append(value).append("&");
            });

            if (!urlBuilder.isEmpty() && urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }

        }
        return restTemplate().exchange(urlBuilder.toString(), HttpMethod.GET, entity, String.class);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

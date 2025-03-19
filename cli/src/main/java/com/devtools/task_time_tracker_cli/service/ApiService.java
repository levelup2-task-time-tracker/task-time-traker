package com.devtools.task_time_tracker_cli.service;

import com.devtools.task_time_tracker_cli.component.AuthToken;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;


@Service
public class ApiService {
    public static final String url = "http://ec2-13-247-181-133.af-south-1.compute.amazonaws.com:8080/";

    @Autowired
    private AuthToken token;

    public AuthToken getToken() {
        return token;
    }

    public boolean authenticate(){
        return !token.isAuthenticated();
    }

    public <T> ResponseEntity<T> sendRequest(Class<T> responseType, HttpMethod method, String endPoint, Map<String, Object> params){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url + endPoint);
        if (params != null) {
            params.forEach(uriBuilder::queryParam);
        }
        String finalUrl = uriBuilder.build().toUriString();

        return restTemplate().exchange(finalUrl, method, entity, responseType);
    }

    public List<Map<String, Object>> jsonArrayHandler(ResponseEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {});
        }catch (Exception e){
            throw new RuntimeException("Failed to parse response to JSON", e);
        }
    }

    public String displayResponseArray(List<Map<String, Object>> response, String message){
        if (response == null || response.isEmpty()) {
            return "Response is empty.";
        }

        StringBuilder result = new StringBuilder(message).append(":\n");
        for (Map<String, Object> item : response) {
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
            result.setLength(result.length() - 2); // Remove last comma and space
            result.append("\n");
        }

        return result.toString();
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

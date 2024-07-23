package com.finalproject.finsera.finsera.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class ApiSandboxUtils {


    private RestTemplate restTemplate;


    @Value("${bca.api.url}")
    private String baseUrl;

    public Object headerAndBodySetting(
            String[] headers,
            String[] headersValues,
            String url,
            HttpMethod httpMethod,
            String[] body,
            String[] bodyValues) {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formattedNow = now.format(formatter);
        HttpHeaders headerVar = new HttpHeaders();
        headerVar.setAccept(List.of(MediaType.ALL));
        headerVar.setContentType(MediaType.APPLICATION_JSON);
        headerVar.setConnection("keep-alive");
        ResponseEntity<?> response = null;
        String urlTarget = baseUrl + url;
        for (int i = 0; i < headers.length; i++) {
            headerVar.set(headers[i], headersValues[i]);
        }
        if(body.length > 0) {
            HttpEntity<Map<String, String>> entity = null;
            for (int i = 0; i < body.length; i++) {
                Map<String, String> bodyVar = new HashMap<>();
                bodyVar.put(body[i], bodyValues[i]);
                entity = new HttpEntity<>(bodyVar, headerVar);
            }
           response = restTemplate.exchange(
                    urlTarget,
                    httpMethod,
                    entity,
                    new ParameterizedTypeReference<>(){}
            );
        } else {
            HttpEntity<String> entity = new HttpEntity<>(headerVar);
            response = restTemplate.exchange(
                    urlTarget,
                    httpMethod,
                    entity,
                    new ParameterizedTypeReference<>(){}
            );
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Response Headers: {}", response.getHeaders());
            log.info("Response Status Code: {}", response.getStatusCodeValue());
            log.info("Response Body: {}", response.getBody());
        } else {
            log.error("Error: Status Code: {}", response.getStatusCodeValue());
        }

        return response.getBody();
    }
}

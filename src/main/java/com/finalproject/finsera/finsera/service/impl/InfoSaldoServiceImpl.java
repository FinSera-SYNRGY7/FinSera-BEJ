package com.finalproject.finsera.finsera.service.impl;

import com.finalproject.finsera.finsera.dto.InfoSaldoResponse;
import com.finalproject.finsera.finsera.dto.base.BaseResponse;
import com.finalproject.finsera.finsera.service.InfoSaldoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class InfoSaldoServiceImpl implements InfoSaldoService {


    @Autowired
    private RestTemplate restTemplate;


    @Value("${bca.api.url}")
    private String baseUrl;


    @Override
    public Map getInfoSaldo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setConnection("keep-alive");
        headers.set("X-CLIENT-KEY", "66da6169-5392-4ffc-b3a4-8cf437b8c988");
        headers.set("X-TIMESTAMP", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        headers.set("Private_Key", "-----BEGIN PRIVATE KEY----- MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCe5ktDYufsqjv8HH78fzloRa6DjuDMMD8YxVCc3uQYOPxN2IWb2cNs/pG3NnO6cfuqnEvUgvougsUMhVUVl1n670pA2B2eiOQdsYPNxZsSUVPHUjg1ZHSWn5yNxMZ9MIZv1DhZVzdiRMtAw66jTyF9BleCcjq/oW3uHqKWGtepOJVHNtgLz9MGJTzEXb0uEOOK0uCuJK+VMMqKXlUhMZur2Bah99D6vuMnmpmCnNQEvVj4MqhFQHD4uzW6qq2IevwHVbk8wPOq1HByftBQW/fowdtJKfSRaUf6dShHBrdcHDWlu+5TOB7LcbUY7oKFDk8NGdrHQiRaKlgLfU2BGHVVAgMBAAECggEAUseq4fo+1N6CzX6S8TveTmIu3j6rAfUIigERVAgSUEQvvOZWBLFXzAp7IzVs6O7Eq0ctghKR/3UE7tbvUoY8zCupRUrRc2vhW07FWYfel5ZizO4adkZVLrsMNhcTSNjk0JGAoZp8Meeg86Z97nok+hs5r62OyZJx0KGJFiX5wB/ziNwBlrc5asxGk4DhwGsSILrpi+3/xTdTeUu1kAnSREHSEgunGW+VFE/lGXiG8gd254m6MW8nRlaKnSODlZd3+YRKbbRUAaZ/GyTdwMZZrptn9H+ZSRMl0Z9sS0pe8LmJM5eqiCjqCLaxeJ/nEQeJm5x3L1JQpGsnGVfZjDZowQKBgQDrevLyjZzFFJisTMBV1pe+KQuP0hF9SJqgzOXeMMMxnM93kC4obimgAcmJmiHUnWWLt1UGyut96kUZIkdZg3rj/rBrk2uKrnyrgdbhEtPtfWGJFSItLfcZcTBr+VoGWqqKEMhMFcVqxFTA8sY3Xzv6vzcrPtGtX0Q9PEj1Qo9A2QKBgQCsvv89kz98PYl+qx2oHZEXQcyEeGI6ibfDq98EYpoYjcflNSpzQm/QHNtLQLiJ9t5TW1gi/cNry24FiJTADw4O/6k3wIbuAIm7HsjUVwJYoeTtn98Q2Ymsksv4yu7K/F8qXb6ELIlYlXOV19ZERcJLKbbQ2yl88Ww7+i0reu0K3QKBgAn/45c3OkQINt+CNtyuSy1REuOdmQ6H6cEQUmaYDYHq1ciO/9bJrszTpppISE1+DZTcSSkLrupe62ZA1WTQt4Q9CYLX9MYj2Llzvws5wHQiUeT/V78xZ3/WFadQJGmGqh1Izyij+Akroym6ZX5udd6VBiO4/DBvjjdHexWnKOwpAoGBAJ5/0OnKhWGVhOa4UsnB9zKDqQeS/W4Alp/uvv3jCsikrljcY0rGFpm5IGz3wVq1LGEHWuMgO4JYcWaaXwGpzphsc/M3r5YI4FbUdCiAfSKdyNNO8Pkg4HV7a7OnX1rYHOlegkP8KTkiR5+hHnQeHZuhdqBDttlxGoIdlfxjGcPxAoGBALSkWh2WzX+rt5Q8jA2nPoj01Je2KXt1NkwWVddaYhI4M65Wsdi2qvi5nradXzOs2p2E/VuoJ0nDotPPxrzdygZ3at0teyGlYEAnfIJPacA3G9FD73smM/2CkVAbaMVt6kNZuVFypnEhMbf6A8SKbH0/N4QBo0GBAXH7OmA0hkSp -----END PRIVATE KEY-----");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = baseUrl + "/api/v1/utilities/signature-auth";
        ResponseEntity<InfoSaldoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                InfoSaldoResponse.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Response Headers: {}", response.getHeaders());
            log.info("Response Status Code: {}", response.getStatusCodeValue());
            log.info("Response Body: {}", response.getBody().getSignature());
        } else {
            log.error("Error: Status Code: {}", response.getStatusCodeValue());
        }



        return null;
    }
}

package com.fullscriptintegration.fullscript.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestHttpUtility {

    @Value("${fullscript.api.url}")
    private String apiUrl;

    public static Object postRequest(
            Object dataRequest,
            String url,
            Class<?> responseClass,
            @Nullable String token,
            RestTemplate restTemplate
    ) throws JsonProcessingException {
        return request(dataRequest, url, responseClass, token, restTemplate, HttpMethod.POST);
    }

    public static Object patchRequest(
            Object dataRequest,
            String url,
            Class<Object> responseClass,
            @Nullable String token,
            RestTemplate restTemplate
    ) throws JsonProcessingException {
        return request(dataRequest, url, responseClass, token, restTemplate, HttpMethod.PATCH);
    }

    public static <T> Object getRequest(
            String url,
            Class<T> responseClass,
            @Nullable String token,
            RestTemplate restTemplate
    ) throws JsonProcessingException {
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<Void>(createHeaderWithToken(token));

            ResponseEntity<String> responseResponseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, httpEntity, String.class
            );

            Object response = JSONUtility.toObject(
                    responseResponseEntity.getBody(), responseClass
            );

            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Object deleteRequestObs(
            String url,
            Class<Object> responseClass,
            @Nullable String token,
            RestTemplate restTemplate
    ) throws Exception {
        try {
            Object response = deleteRequest(
                    url,
                    responseClass,
                    token,
                    restTemplate
            );
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Object deleteRequest(
            String url,
            Class<Object> responseClass,
            @Nullable String token,
            RestTemplate restTemplate
    ) throws Exception {
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<Void>(createHeaderWithToken(token));

            ResponseEntity<String> responseResponseEntity = restTemplate.exchange(
                    url, HttpMethod.DELETE, httpEntity, String.class
            );

            Object response = JSONUtility.toObject(
                    responseResponseEntity.getBody(), responseClass
            );

            return response;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static Object getTransactionEventHistory(
            String url,
            Class<Object> responseClass,
            @Nullable String token,
            RestTemplate restTemplate
    ) throws Exception {
        try {
            HttpEntity<Void> httpEntity = new HttpEntity<Void>(createHeaderWithToken(token));

            ResponseEntity<String> responseResponseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, httpEntity, String.class
            );

            Object response = JSONUtility.toObject(
                    responseResponseEntity.getBody(), responseClass
            );

            return response;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public String getUrl(String endpoint) {
        return apiUrl + endpoint;
    }

    private static HttpHeaders createHeaderWithToken(@Nullable String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            httpHeaders.add("Authorization", "Bearer " + token);
        }

        return httpHeaders;
    }

    private static <T, U> Object request(
            U dataRequest,
            String url,
            Class<T> responseClass,
            String token,
            RestTemplate restTemplate,
            HttpMethod httpMethod
    ) throws JsonProcessingException {
        try {
                HttpEntity<U> httpEntity = new HttpEntity<>(dataRequest, createHeaderWithToken(token));

                ResponseEntity<String> responseResponseEntity = restTemplate.exchange(
                        url, httpMethod, httpEntity, String.class
                );

                Object response = JSONUtility.toObject(
                        responseResponseEntity.getBody(), responseClass
                );
                return response;
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}

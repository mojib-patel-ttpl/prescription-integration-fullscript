package com.fullscriptintegration.fullscript.integration.service.fullscript;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizeService {

    @Value("${fullscript.client.id}")
    private String clientId;

    @Value("${fullscript.client.secret}")
    private String clientSecret;

    @Value("${fullscript.client.redirect-url}")
    private String redirectUrl;

    public String generateAuthUrl(UUID userUUID) {
        String authString = "https://us-snd.fullscript.io/oauth/authorize?client_id=" +
                clientId + "&" +
                "redirect_uri=" + redirectUrl + "&" +
                "state=" + userUUID;
        return authString;
    }
}

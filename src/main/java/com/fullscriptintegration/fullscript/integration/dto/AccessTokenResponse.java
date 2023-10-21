package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AccessTokenResponse {

    private OAuth oauth;

    @Data
    public class OAuth {
        private String access_token;
        private String token_type;
        private long expires_in;
        private String refresh_token;
        private String scope;
        private String created_at;
        private ResourceOwner resource_owner;
    }
}

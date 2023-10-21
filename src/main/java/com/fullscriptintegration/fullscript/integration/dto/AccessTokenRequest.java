package com.fullscriptintegration.fullscript.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String code;
    private String refresh_token;
    private String redirect_uri;
}

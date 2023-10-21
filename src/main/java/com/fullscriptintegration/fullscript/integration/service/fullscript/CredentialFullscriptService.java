package com.fullscriptintegration.fullscript.integration.service.fullscript;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullscriptintegration.fullscript.integration.dto.AccessTokenRequest;
import com.fullscriptintegration.fullscript.integration.dto.AccessTokenResponse;
import com.fullscriptintegration.fullscript.integration.entity.UserCodeEntity;
import com.fullscriptintegration.fullscript.integration.repository.UserCodeRepository;
import com.fullscriptintegration.fullscript.integration.util.RequestHttpUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class CredentialFullscriptService {

    @Value("${fullscript.client.id}")
    private String clientId;

    @Value("${fullscript.client.secret}")
    private String clientSecret;

    @Value("${fullscript.client.redirect-url}")
    private String redirectUrl;

    @Autowired
    private RequestHttpUtility requestHttpUtility;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserCodeRepository codeRepository;

    public UserCodeEntity getAccessToken(UserCodeEntity userCodeEntity) throws JsonProcessingException {
        if(isTokenValid(userCodeEntity)) return userCodeEntity;
        else if(StringUtils.isNotBlank(userCodeEntity.getRefreshToken())) {
            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) RequestHttpUtility
                    .postRequest(new AccessTokenRequest("refresh_token", clientId, clientSecret, null, userCodeEntity.getRefreshToken(), redirectUrl),
                            requestHttpUtility.getUrl("/oauth/token"),
                            AccessTokenResponse.class,
                            null, restTemplate
                    );
            return codeRepository.save(updateUser(userCodeEntity, accessTokenResponse));
        }
        else {
            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) RequestHttpUtility
                    .postRequest(new AccessTokenRequest("authorization_code", clientId, clientSecret, userCodeEntity.getAccessCode(), null, redirectUrl),
                    requestHttpUtility.getUrl("/oauth/token"),
                    AccessTokenResponse.class,
                    null, restTemplate
                    );
            return codeRepository.save(updateUser(userCodeEntity, accessTokenResponse));
        }
    }

    private UserCodeEntity updateUser(UserCodeEntity userCodeEntity, AccessTokenResponse accessTokenResponse) {
        userCodeEntity.setAccessToken(accessTokenResponse.getOauth().getAccess_token());
        userCodeEntity.setRefreshToken(accessTokenResponse.getOauth().getRefresh_token());
        userCodeEntity.setExpireInMs(accessTokenResponse.getOauth().getExpires_in());
        userCodeEntity.setTokenCreationInstant(Instant.parse(accessTokenResponse.getOauth().getCreated_at()));
        return userCodeEntity;
    }

    @JsonIgnore
    public boolean isTokenValid(UserCodeEntity userCodeEntity) {
        if (userCodeEntity == null || userCodeEntity.getAccessToken() == null || userCodeEntity.getTokenCreationInstant() == null) {
            return false;
        }
        long diff = Instant.now().toEpochMilli() - userCodeEntity.getTokenCreationInstant().toEpochMilli();
        return diff < (1000 * userCodeEntity.getExpireInMs());
    }
}

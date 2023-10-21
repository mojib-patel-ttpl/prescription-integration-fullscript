package com.fullscriptintegration.fullscript.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullscriptintegration.fullscript.integration.dto.AccessTokenRequest;
import com.fullscriptintegration.fullscript.integration.dto.AccessTokenResponse;
import com.fullscriptintegration.fullscript.integration.dto.ClinicResponse;
import com.fullscriptintegration.fullscript.integration.entity.UserCodeEntity;
import com.fullscriptintegration.fullscript.integration.exception.FullscriptThinkitiveException;
import com.fullscriptintegration.fullscript.integration.repository.UserCodeRepository;
import com.fullscriptintegration.fullscript.integration.service.fullscript.CredentialFullscriptService;
import com.fullscriptintegration.fullscript.integration.util.RequestHttpUtility;
import com.fullscriptintegration.fullscript.integration.util.Response;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Service
public class ClinicService {

    @Autowired
    private CredentialFullscriptService credentialFullscriptService;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RequestHttpUtility requestHttpUtility;


    public ClinicResponse getClinic(UUID userUUID) throws FullscriptThinkitiveException, JsonProcessingException {
        if(Objects.isNull(userUUID)) throw new FullscriptThinkitiveException(ResponseCode.BAD_REQUEST, "User UUID is manadatory!");
        UserCodeEntity userCodeEntity = userCodeRepository.findByUserUUID(userUUID).orElseThrow(() -> new FullscriptThinkitiveException(ResponseCode.BAD_REQUEST, "User data does not exists for given User UUID!"));
        userCodeEntity = credentialFullscriptService.getAccessToken(userCodeEntity);

        return (ClinicResponse) RequestHttpUtility.getRequest(requestHttpUtility.getUrl("/clinic"),
                        ClinicResponse.class,
                        userCodeEntity.getAccessToken(),
                        restTemplate);

    }
}

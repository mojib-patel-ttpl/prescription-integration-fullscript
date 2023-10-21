package com.fullscriptintegration.fullscript.integration.service;

import com.fullscriptintegration.fullscript.integration.dto.PractitionerPaginationResponse;
import com.fullscriptintegration.fullscript.integration.entity.UserCodeEntity;
import com.fullscriptintegration.fullscript.integration.exception.FullscriptThinkitiveException;
import com.fullscriptintegration.fullscript.integration.repository.UserCodeRepository;
import com.fullscriptintegration.fullscript.integration.service.fullscript.CredentialFullscriptService;
import com.fullscriptintegration.fullscript.integration.util.RequestHttpUtility;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Service
public class PractitionerService {

    @Autowired
    private CredentialFullscriptService credentialFullscriptService;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RequestHttpUtility requestHttpUtility;

    public PractitionerPaginationResponse getAllPractitioners(Pageable pageable, UUID userUUID) throws Exception {
        if(Objects.isNull(userUUID)) throw new FullscriptThinkitiveException(ResponseCode.BAD_REQUEST, "User UUID is manadatory!");
        UserCodeEntity userCodeEntity = userCodeRepository.findByUserUUID(userUUID).orElseThrow(() -> new FullscriptThinkitiveException(ResponseCode.BAD_REQUEST, "User data does not exists for given User UUID!"));
        userCodeEntity = credentialFullscriptService.getAccessToken(userCodeEntity);

        return (PractitionerPaginationResponse) RequestHttpUtility.getRequest(requestHttpUtility.getUrl("/clinic/practitioners?page[number]="+pageable.getPageNumber()+"&page[size]="+pageable.getPageSize()),
                PractitionerPaginationResponse.class,
                userCodeEntity.getAccessToken(),
                restTemplate);
    }

}

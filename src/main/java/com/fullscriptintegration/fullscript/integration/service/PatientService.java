package com.fullscriptintegration.fullscript.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullscriptintegration.fullscript.integration.dto.*;
import com.fullscriptintegration.fullscript.integration.entity.UserCodeEntity;
import com.fullscriptintegration.fullscript.integration.exception.FullscriptThinkitiveException;
import com.fullscriptintegration.fullscript.integration.repository.UserCodeRepository;
import com.fullscriptintegration.fullscript.integration.service.fullscript.CredentialFullscriptService;
import com.fullscriptintegration.fullscript.integration.util.RequestHttpUtility;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private CredentialFullscriptService credentialFullscriptService;

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RequestHttpUtility requestHttpUtility;

    private UserCodeEntity getUserCodeEntity(UUID userUUID) throws FullscriptThinkitiveException, JsonProcessingException {
        if(Objects.isNull(userUUID)) throw new FullscriptThinkitiveException(ResponseCode.BAD_REQUEST, "User UUID is manadatory!");
        UserCodeEntity userCodeEntity = userCodeRepository.findByUserUUID(userUUID).orElseThrow(() -> new FullscriptThinkitiveException(ResponseCode.BAD_REQUEST, "User data does not exists for given User UUID!"));
        userCodeEntity = credentialFullscriptService.getAccessToken(userCodeEntity);
        return userCodeEntity;
    }

    public PatientPaginationResponse getAllPatients(Pageable pageable, UUID userUUID) throws Exception{
        UserCodeEntity userCodeEntity = getUserCodeEntity(userUUID);

        return (PatientPaginationResponse) RequestHttpUtility.getRequest(requestHttpUtility.getUrl("/clinic/patients?page[number]="+pageable.getPageNumber()+"&page[size]="+pageable.getPageSize()),
                PatientPaginationResponse.class,
                userCodeEntity.getAccessToken(),
                restTemplate);
    }

    public PatientResponseWrapper createPatient(UUID userUUID, PatientRequest patientRequest) throws Exception{
        UserCodeEntity userCodeEntity = getUserCodeEntity(userUUID);
        return (PatientResponseWrapper) RequestHttpUtility.postRequest(patientRequest,
                requestHttpUtility.getUrl("/clinic/patients"),
                PatientResponseWrapper.class,
                userCodeEntity.getAccessToken(),
                restTemplate);
    }

    public PatientResponseWrapper getPatientByPatientId(UUID userUUID, String id) throws Exception{
        UserCodeEntity userCodeEntity = getUserCodeEntity(userUUID);

        return (PatientResponseWrapper) RequestHttpUtility.getRequest(requestHttpUtility.getUrl("/clinic/patients/"+id),
                PatientResponseWrapper.class,
                userCodeEntity.getAccessToken(),
                restTemplate);
    }
}

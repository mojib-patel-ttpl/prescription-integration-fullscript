package com.fullscriptintegration.fullscript.integration.service;

import com.fullscriptintegration.fullscript.integration.dto.UserCode;
import com.fullscriptintegration.fullscript.integration.entity.UserCodeEntity;
import com.fullscriptintegration.fullscript.integration.exception.FullscriptThinkitiveException;
import com.fullscriptintegration.fullscript.integration.repository.UserCodeRepository;
import com.fullscriptintegration.fullscript.integration.service.fullscript.AuthorizeService;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserCodeService {

    @Autowired
    private UserCodeRepository userCodeRepository;

    @Autowired
    private AuthorizeService authorizeService;

    @Autowired
    private ModelMapper modelMapper;

    public UserCode connectUser(UserCode userCode) throws FullscriptThinkitiveException {
        //check user already present for given uuid
        Optional<UserCodeEntity> userCodeEntityOptional = userCodeRepository.findByUserUUID(userCode.getUserUUID());
        if(userCodeEntityOptional.isPresent()) {
            if(userCodeEntityOptional.get().isConnectedToFullScript()) throw new FullscriptThinkitiveException(ResponseCode.USER_ALREADY_EXIST, "User is already connected with Fullscript!");
            else {
                UserCode userCodeResponse = modelMapper.map(userCodeEntityOptional, UserCode.class);
                userCodeResponse.setAuthUrl(authorizeService.generateAuthUrl(userCodeEntityOptional.get().getUserUUID()));
                return userCodeResponse;
            }
        }
        else {
            UserCodeEntity userCodeEntity = new UserCodeEntity();
            userCodeEntity = modelMapper.map(userCode, UserCodeEntity.class);
            userCodeEntity.setUuid(UUID.randomUUID());
            userCodeEntity.setConnectedToFullScript(false);

            UserCode userCodeResponse = modelMapper.map(userCodeEntity, UserCode.class);
            userCodeResponse.setAuthUrl(authorizeService.generateAuthUrl(userCodeRepository.save(userCodeEntity).getUserUUID()));
            return userCodeResponse;
        }
    }

    public UserCode updateFullScriptCode(UserCode userCode) {
        UserCodeEntity userCodeEntity = userCodeRepository.findByUserUUID(userCode.getUserUUID()).orElseThrow(() -> new EntityNotFoundException("UserUUID is invalid!"));
        userCodeEntity.setAccessCode(userCode.getAccessCode());
        userCodeEntity.setConnectedToFullScript(true);
        return modelMapper.map(userCodeRepository.save(userCodeEntity), UserCode.class);
    }
}

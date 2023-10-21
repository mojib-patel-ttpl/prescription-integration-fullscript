package com.fullscriptintegration.fullscript.integration.controller;

import com.fullscriptintegration.fullscript.integration.dto.UserCode;
import com.fullscriptintegration.fullscript.integration.exception.FullscriptThinkitiveException;
import com.fullscriptintegration.fullscript.integration.service.UserCodeService;
import com.fullscriptintegration.fullscript.integration.util.AppController;
import com.fullscriptintegration.fullscript.integration.util.Response;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user-code")
public class UserCodeController extends AppController {

    @Autowired
    private UserCodeService userCodeService;

    @PostMapping("/connect")
    public ResponseEntity<Response> connectUser(@RequestBody UserCode userCode) throws FullscriptThinkitiveException {
        return success(ResponseCode.OK, "Successfully connected user to fullscript", userCodeService.connectUser(userCode));
    }

    @PutMapping("/save")
    public ResponseEntity<Response> saveFullScriptCode(@RequestBody UserCode userCode) {
        return success(ResponseCode.UPDATED, "Successfully saved fullscript auth code", userCodeService.updateFullScriptCode(userCode));
    }

}

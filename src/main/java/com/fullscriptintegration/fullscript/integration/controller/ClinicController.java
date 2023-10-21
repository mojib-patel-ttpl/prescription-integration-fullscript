package com.fullscriptintegration.fullscript.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullscriptintegration.fullscript.integration.exception.FullscriptThinkitiveException;
import com.fullscriptintegration.fullscript.integration.service.ClinicService;
import com.fullscriptintegration.fullscript.integration.util.AppController;
import com.fullscriptintegration.fullscript.integration.util.Response;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/clinic")
public class ClinicController extends AppController {

    @Autowired
    private ClinicService clinicService;

    @GetMapping
    public ResponseEntity<Response> getClinic(@RequestParam UUID userUUID) throws FullscriptThinkitiveException, JsonProcessingException {
        return data(ResponseCode.OK, "Successfully fetched the clinic from Fullscript!", clinicService.getClinic(userUUID));
    }
}

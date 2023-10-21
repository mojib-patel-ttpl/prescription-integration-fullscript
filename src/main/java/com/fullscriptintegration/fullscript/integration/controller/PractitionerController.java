package com.fullscriptintegration.fullscript.integration.controller;

import com.fullscriptintegration.fullscript.integration.service.PractitionerService;
import com.fullscriptintegration.fullscript.integration.util.AppController;
import com.fullscriptintegration.fullscript.integration.util.Response;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/practitioner")
public class PractitionerController extends AppController {

    @Autowired
    private PractitionerService practitionerService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllPractitioner(Pageable pageable, @RequestParam UUID userUUID) throws Exception {
        return data(ResponseCode.OK, "Successfully fetched all practitioners", practitionerService.getAllPractitioners(pageable, userUUID));
    }
}

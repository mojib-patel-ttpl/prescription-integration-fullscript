package com.fullscriptintegration.fullscript.integration.controller;

import com.fullscriptintegration.fullscript.integration.dto.PatientRequest;
import com.fullscriptintegration.fullscript.integration.service.PatientService;
import com.fullscriptintegration.fullscript.integration.util.AppController;
import com.fullscriptintegration.fullscript.integration.util.Response;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController extends AppController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllPatients(Pageable pageable, @RequestParam UUID userUUID) throws Exception{
        return data(ResponseCode.OK, "Successfully fetched all the Patients for given user!", patientService.getAllPatients(pageable, userUUID));
    }

    @PostMapping
    public ResponseEntity<Response> createPatient(@RequestParam UUID userUUID, @RequestBody @Valid @javax.validation.Valid PatientRequest patientRequest) throws Exception {
        return success(ResponseCode.CREATED, "Successfully created Patient!", patientService.createPatient(userUUID, patientRequest));
    }

    @GetMapping("/patientId/{id}")
    public ResponseEntity<Response> getPatientByPatientId(@PathVariable String id, @RequestParam UUID userUUID) throws Exception {
        return data(ResponseCode.OK, "Successfully fetched patient by PatientId!", patientService.getPatientByPatientId(userUUID, id));
    }
}

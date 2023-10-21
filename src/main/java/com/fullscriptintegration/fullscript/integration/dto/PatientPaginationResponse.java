package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PatientPaginationResponse {

    private List<PatientResponse> patients = new ArrayList<>();
    private Meta meta;
}

package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PractitionerPaginationResponse {
    private List<PractitionerResponse> practitioners = new ArrayList<>();
    private Meta meta;
}

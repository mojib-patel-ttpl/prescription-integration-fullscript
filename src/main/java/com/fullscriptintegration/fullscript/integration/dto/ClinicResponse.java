package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

@Data
public class ClinicResponse {

    private Clinic clinic;

    @Data
    public class Clinic {
        private String id;
        private String name;
        private long patient_count;
        private long practitioner_count;
        private long discount;
        private String dispensary_url;
        private String integration_id;
        private String integration_activated_at;
        private String margin_type;
    }
}

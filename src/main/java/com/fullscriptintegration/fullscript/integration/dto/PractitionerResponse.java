package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

@Data
public class PractitionerResponse {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String practitioner_type_id;
    private Boolean invite_accepted;
    private MetaData metadata;

    @Data
    public class MetaData {
        private String id;
    }
}

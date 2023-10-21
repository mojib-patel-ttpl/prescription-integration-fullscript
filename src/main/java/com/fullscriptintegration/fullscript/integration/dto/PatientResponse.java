package com.fullscriptintegration.fullscript.integration.dto;

import lombok.Data;

@Data
public class PatientResponse {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private boolean archived;
    private String date_of_birth;
    private String gender;
    private int discount;
    private int total_discount;
    private String mobile_number;
    private String text_message_notification;
    private Metadata metadata;
}

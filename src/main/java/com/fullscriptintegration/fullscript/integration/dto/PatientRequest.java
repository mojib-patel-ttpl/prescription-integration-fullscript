package com.fullscriptintegration.fullscript.integration.dto;

import com.fullscriptintegration.fullscript.integration.dto.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import lombok.Data;

@Data
public class PatientRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String date_of_birth;

    private Gender gender;

    @Pattern(regexp = "\\+\\d{11}")
    private String mobile_number;

    private boolean send_welcome_email = true;

    @Min(value = 0, message = "Discount cannot be less than 0")
    @Max(value = 100, message = "Discount cannot be greater than 100")
    private int discount;

    private Metadata metadata;
}

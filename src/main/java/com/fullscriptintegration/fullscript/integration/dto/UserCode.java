package com.fullscriptintegration.fullscript.integration.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserCode {
    private UUID uuid;

    @NotNull
    private UUID userUUID;

    private boolean connectedToFullScript;
    private String accessCode;
    private String authUrl;
    private boolean active = true;
    private boolean deleted = false;
    private Instant createdAt;
    private Instant modifiedAt;
}

package com.fullscriptintegration.fullscript.integration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;


@Entity
@Data
@Table(name = "user_code")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    @Column(name = "user_uuid", nullable = false, unique = true)
    private UUID userUUID;

    private boolean connectedToFullScript;
    private String accessCode;

    private String accessToken;
    private String refreshToken;
    private long expireInMs;
    private Instant tokenCreationInstant;

    private boolean active;
    private boolean deleted;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;
}

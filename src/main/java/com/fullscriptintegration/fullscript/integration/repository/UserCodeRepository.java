package com.fullscriptintegration.fullscript.integration.repository;

import com.fullscriptintegration.fullscript.integration.entity.UserCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCodeRepository extends JpaRepository<UserCodeEntity, Long> {
    Optional<UserCodeEntity> findByUserUUID(UUID userUUID);
}

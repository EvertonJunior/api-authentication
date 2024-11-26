package com.ej.authentication.repository;

import com.ej.authentication.entities.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    List<ResetPasswordToken> findByUsername(String username);
    Optional<ResetPasswordToken> findByToken(String token);

}

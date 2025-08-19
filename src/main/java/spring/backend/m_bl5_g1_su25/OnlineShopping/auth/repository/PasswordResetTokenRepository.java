package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.PasswordResetToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    // Find valid token (not used and not expired)
    @Query("SELECT p FROM PasswordResetToken p WHERE p.token = :token AND p.used = false AND p.expiryTime > :currentTime")
    Optional<PasswordResetToken> findValidToken(@Param("token") String token, @Param("currentTime") LocalDateTime currentTime);

    // Find token by token string
    Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);

    // Find valid token by email (for checking if user already has pending reset)
    @Query("SELECT p FROM PasswordResetToken p WHERE p.email = :email AND p.used = false AND p.expiryTime > :currentTime ORDER BY p.createdAt DESC")
    Optional<PasswordResetToken> findValidTokenByEmail(@Param("email") String email, @Param("currentTime") LocalDateTime currentTime);

    // Invalidate all previous tokens for an email (cleanup when new token is generated)
    @Modifying
    @Query("UPDATE PasswordResetToken p SET p.used = true WHERE p.email = :email AND p.used = false")
    void invalidateAllTokensByEmail(@Param("email") String email);

    // Clean up expired tokens (for scheduled cleanup)
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiryTime < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}

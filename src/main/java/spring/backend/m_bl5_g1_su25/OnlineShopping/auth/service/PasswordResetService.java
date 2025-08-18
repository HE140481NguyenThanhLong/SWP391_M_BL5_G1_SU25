package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.PasswordResetToken;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.PasswordResetTokenRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.service.EmailService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    // Token validity duration in hours (24 hours as requested)
    private static final int TOKEN_VALIDITY_HOURS = 24;

    @Transactional
    public void sendPasswordResetEmail(String email) {
        // Invalidate all previous tokens for this email
        tokenRepository.invalidateAllTokensByEmail(email);

        // Generate new unique token
        String resetToken = PasswordResetToken.generateToken();

        // Create and save reset token
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .email(email)
                .token(resetToken)
                .expiryTime(LocalDateTime.now().plusHours(TOKEN_VALIDITY_HOURS))
                .used(false)
                .build();

        tokenRepository.save(passwordResetToken);

        // Send reset email with token link
        emailService.sendPasswordResetEmail(email, resetToken);

        log.info("Password reset token generated and email sent successfully for email: {}", email);
    }

    @Transactional
    public String validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository
                .findValidToken(token, LocalDateTime.now());

        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        // Return email associated with this token for password reset
        return resetToken.getEmail();
    }

    @Transactional
    public void markTokenAsUsed(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByTokenAndUsedFalse(token);

        if (tokenOpt.isPresent()) {
            PasswordResetToken resetToken = tokenOpt.get();
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);

            log.info("Password reset token marked as used for email: {}", resetToken.getEmail());
        }
    }

    public boolean hasValidToken(String email) {
        return tokenRepository.findValidTokenByEmail(email, LocalDateTime.now()).isPresent();
    }

    @Transactional
    public void cleanupExpiredTokens() {
        try {
            tokenRepository.deleteExpiredTokens(LocalDateTime.now());
            log.debug("Expired password reset tokens cleaned up successfully");
        } catch (Exception e) {
            log.error("Error cleaning up expired tokens: {}", e.getMessage(), e);
        }
    }
}

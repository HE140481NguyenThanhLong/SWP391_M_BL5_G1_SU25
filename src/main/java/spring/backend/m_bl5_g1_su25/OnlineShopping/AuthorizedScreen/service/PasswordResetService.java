package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.PasswordResetToken;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.PasswordResetTokenRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final AuthorizedRepo userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${spring.mail.username:noreply@smartshop.com}")
    private String fromEmail;

    /**
     * Tạo và gửi email reset password
     */
    @Transactional
    public boolean sendPasswordResetEmail(String email) {
        try {
            // 1. Tìm user theo email
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                log.warn("Password reset requested for non-existent email: {}", email);
                return false;
            }

            // 2. Xóa các token cũ của user này
            tokenRepository.deleteByUser(user);

            // 3. Tạo token mới
            String token = generateResetToken();
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // Token hết hạn sau 1 giờ

            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(expiryDate)
                    .used(false)
                    .build();

            tokenRepository.save(resetToken);

            // 4. Gửi email
            sendResetEmail(user.getEmail(), user.getUsername(), token);

            log.info("Password reset email sent successfully to: {}", email);
            return true;

        } catch (Exception e) {
            log.error("Error sending password reset email to {}: {}", email, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Xác thực và reset password
     */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        try {
            // 1. Tìm token
            PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
            if (resetToken == null) {
                log.warn("Invalid password reset token: {}", token);
                return false;
            }

            // 2. Kiểm tra token có hợp lệ không
            if (!resetToken.isValid()) {
                log.warn("Expired or used password reset token: {}", token);
                return false;
            }

            // 3. Update mật khẩu user
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // 4. Đánh dấu token đã sử dụng
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);

            log.info("Password reset successfully for user: {}", user.getUsername());
            return true;

        } catch (Exception e) {
            log.error("Error resetting password with token {}: {}", token, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Kiểm tra token có hợp lệ không
     */
    public boolean isValidToken(String token) {
        return tokenRepository.findByToken(token)
                .map(PasswordResetToken::isValid)
                .orElse(false);
    }

    /**
     * Lấy user từ token
     */
    public User getUserByToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(PasswordResetToken::isValid)
                .map(PasswordResetToken::getUser)
                .orElse(null);
    }

    /**
     * Tạo token ngẫu nhiên
     */
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Gửi email reset password
     */
    private void sendResetEmail(String toEmail, String username, String token) {
        try {
            String resetLink = baseUrl + "/auth/reset-password?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Password Reset Request");
            message.setText(createEmailContent(username, resetLink));

            mailSender.send(message);
            log.info("Reset email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send reset email to {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Tạo nội dung email
     */
    private String createEmailContent(String username, String resetLink) {
        return String.format(
                "Hi %s,\n\n" +
                "You have requested to reset your password for Smart Shop.\n\n" +
                "Please click the link below to reset your password:\n" +
                "%s\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Smart Shop Team",
                username, resetLink
        );
    }

    /**
     * Cleanup expired tokens (chạy định kỳ)
     */
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            tokenRepository.deleteExpiredTokens(LocalDateTime.now());
            log.info("Expired password reset tokens cleaned up");
        } catch (Exception e) {
            log.error("Error cleaning up expired tokens: {}", e.getMessage(), e);
        }
    }
}

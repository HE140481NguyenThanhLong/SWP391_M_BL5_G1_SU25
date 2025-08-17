package spring.backend.m_bl5_g1_su25.OnlineShopping.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Disabled by default - MockEmailService is @Primary
@Service("realEmailService")
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@flowshop.com}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Flow Shop - Password Reset Request");
            message.setText(buildPasswordResetEmailContent(resetToken));

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send password reset email. Please try again later.");
        }
    }

    public void sendPasswordChangeNotification(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Flow Shop - Password Changed Successfully");
            message.setText(buildPasswordChangeNotificationContent());

            mailSender.send(message);
            log.info("Password change notification sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send password change notification to: {}. Error: {}", toEmail, e.getMessage(), e);
            // Don't throw exception here as password was already changed successfully
        }
    }

    private String buildPasswordResetEmailContent(String resetToken) {
        String resetLink = baseUrl + "/shared/change-password?token=" + resetToken;

        return String.format("""
            Dear User,
            
            You have requested to reset your password for your Flow Shop account.
            
            Please click the link below to reset your password:
            %s
            
            This link will expire in 24 hours. If you did not request this password reset, 
            please ignore this email or contact our support team.
            
            Best regards,
            Flow Shop Team
            """, resetLink);
    }

    private String buildPasswordChangeNotificationContent() {
        return """
            Dear User,
            
            Your Flow Shop account password has been successfully changed.
            
            If you did not make this change, please contact our support team immediately.
            
            Best regards,
            Flow Shop Team
            """;
    }
}

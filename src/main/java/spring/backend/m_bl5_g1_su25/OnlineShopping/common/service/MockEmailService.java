package spring.backend.m_bl5_g1_su25.OnlineShopping.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class MockEmailService {

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        log.info("=== MOCK EMAIL SERVICE ===");
        log.info("TO: {}", toEmail);
        log.info("SUBJECT: Flow Shop - Password Reset Request");
        log.info("RESET TOKEN: {}", resetToken);
        log.info("RESET LINK: http://localhost:8080/shared/change-password?token={}", resetToken);
        log.info("EMAIL CONTENT:");
        log.info("""
            Dear User,
            
            You have requested to reset your password for your Flow Shop account.
            
            Please click the link below to reset your password:
            http://localhost:8080/shared/change-password?token={}
            
            This link will expire in 24 hours. If you did not request this password reset, 
            please ignore this email or contact our support team.
            
            Best regards,
            Flow Shop Team
            """, resetToken);
        log.info("=========================");
    }

    public void sendPasswordChangeNotification(String toEmail) {
        log.info("=== MOCK EMAIL SERVICE ===");
        log.info("TO: {}", toEmail);
        log.info("SUBJECT: Flow Shop - Password Changed Successfully");
        log.info("EMAIL CONTENT:");
        log.info("""
            Dear User,
            
            Your Flow Shop account password has been successfully changed.
            
            If you did not make this change, please contact our support team immediately.
            
            Best regards,
            Flow Shop Team
            """);
        log.info("=========================");
    }
}

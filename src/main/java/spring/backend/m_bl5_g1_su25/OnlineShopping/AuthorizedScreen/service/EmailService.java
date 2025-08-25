package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender mailSender;

public void sendPasswordResetEmail(User user, String token) {
try {
SimpleMailMessage message = new SimpleMailMessage();
message.setTo(user.getEmail());
message.setSubject("Đặt lại mật khẩu - Smart Shop");
String resetLink = "http://localhost:8080/authority/reset-password?token=" + token;
String content = String.format(
    "Xin chào %s,\n\n" +
    "Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.\n\n" +
    "Vui lòng nhấp vào liên kết dưới đây để đặt lại mật khẩu:\n" +
    "%s\n\n" +
    "Liên kết này sẽ hết hạn sau 15 phút.\n\n" +
    "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
    "Trân trọng,\n" +
    "Đội ngũ Smart Shop",
user.getUsername(), resetLink);

message.setText(content);
message.setFrom("smartshopforeveryone@gmail.com");


mailSender.send(message);
}
catch (Exception e) {
System.err.println("Failed to send password reset email: " + e.getMessage());
throw new RuntimeException("Không thể gửi email đặt lại mật khẩu");
}
}
}

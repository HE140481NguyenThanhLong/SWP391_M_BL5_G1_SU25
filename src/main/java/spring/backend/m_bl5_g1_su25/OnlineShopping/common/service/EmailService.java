package spring.backend.m_bl5_g1_su25.OnlineShopping.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Real email service - enabled for production use
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:smartshopforeveryone@gmail.com}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Yêu cầu đặt lại mật khẩu");
            message.setText(buildPasswordResetEmailContent(resetToken));

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}. Error: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại sau.");
        }
    }

    public void sendPasswordChangeNotification(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Mật khẩu đã được thay đổi thành công");
            message.setText(buildPasswordChangeNotificationContent());

            mailSender.send(message);
            log.info("Password change notification sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send password change notification to: {}. Error: {}", toEmail, e.getMessage(), e);
            // Don't throw exception here as password was already changed successfully
        }
    }

    /**
     * Gửi email chào mừng khi người dùng đăng ký tài khoản thành công
     */
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Chào mừng bạn đến với Smart Shop!");
            message.setText(buildWelcomeEmailContent(username));

            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}. Error: {}", toEmail, e.getMessage(), e);
            // Don't throw exception - registration was already successful
        }
    }

    /**
     * Gửi email xác nhận đơn hàng
     */
    public void sendOrderConfirmationEmail(String toEmail, String customerName, String orderId, String totalAmount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Xác nhận đơn hàng #" + orderId);
            message.setText(buildOrderConfirmationEmailContent(customerName, orderId, totalAmount));

            mailSender.send(message);
            log.info("Order confirmation email sent successfully to: {} for order: {}", toEmail, orderId);

        } catch (Exception e) {
            log.error("Failed to send order confirmation email to: {}. Error: {}", toEmail, e.getMessage(), e);
        }
    }

    /**
     * Gửi email thông báo trạng thái đơn hàng
     */
    public void sendOrderStatusUpdateEmail(String toEmail, String customerName, String orderId, String newStatus) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Cập nhật đơn hàng #" + orderId);
            message.setText(buildOrderStatusUpdateEmailContent(customerName, orderId, newStatus));

            mailSender.send(message);
            log.info("Order status update email sent successfully to: {} for order: {}", toEmail, orderId);

        } catch (Exception e) {
            log.error("Failed to send order status update email to: {}. Error: {}", toEmail, e.getMessage(), e);
        }
    }

    /**
     * Gửi email khuyến mãi
     */
    public void sendPromotionEmail(String toEmail, String customerName, String promotionTitle, String promotionDetails, String discountCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - " + promotionTitle);
            message.setText(buildPromotionEmailContent(customerName, promotionTitle, promotionDetails, discountCode));

            mailSender.send(message);
            log.info("Promotion email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send promotion email to: {}. Error: {}", toEmail, e.getMessage(), e);
        }
    }

    /**
     * Gửi email thông báo sản phẩm có hàng trở lại
     */
    public void sendProductRestockNotification(String toEmail, String customerName, String productName, String productUrl) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Smart Shop - Sản phẩm " + productName + " đã có hàng trở lại!");
            message.setText(buildProductRestockEmailContent(customerName, productName, productUrl));

            mailSender.send(message);
            log.info("Product restock notification sent successfully to: {} for product: {}", toEmail, productName);

        } catch (Exception e) {
            log.error("Failed to send product restock notification to: {}. Error: {}", toEmail, e.getMessage(), e);
        }
    }

    private String buildPasswordResetEmailContent(String resetToken) {
        String resetLink = baseUrl + "/shared/change-password?token=" + resetToken;

        return String.format("""
            Kính chào Quý khách hàng,
            
            Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản Smart Shop của bạn.
            
            Vui lòng nhấp vào liên kết bên dưới để đặt lại mật khẩu:
            %s
            
            Liên kết này sẽ hết hạn sau 24 giờ. Nếu bạn không yêu cầu đặt lại mật khẩu, 
            vui lòng bỏ qua email này hoặc liên hệ với đội ngũ hỗ trợ của chúng tôi.
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """, resetLink);
    }

    private String buildPasswordChangeNotificationContent() {
        return """
            Kính chào Quý khách hàng,
            
            Mật khẩu tài khoản Smart Shop của bạn đã được thay đổi thành công.
            
            Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ ngay với đội ngũ hỗ trợ 
            của chúng tôi tại địa chỉ smartshopforeveryone@gmail.com.
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """;
    }

    private String buildWelcomeEmailContent(String username) {
        return String.format("""
            Kính chào %s,
            
            Chào mừng bạn đã gia nhập cộng đồng Smart Shop!
            
            Tài khoản của bạn đã được tạo thành công. Bạn có thể bắt đầu mua sắm ngay bây giờ với:
            
            🛍️ Hàng ngàn sản phẩm chất lượng cao
            🚚 Giao hàng nhanh chóng trên toàn quốc  
            💝 Ưu đãi độc quyền cho thành viên
            🔒 Thanh toán an toàn, bảo mật
            
            Khám phá ngay: %s
            
            Cảm ơn bạn đã lựa chọn Smart Shop!
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """, username, baseUrl);
    }

    private String buildOrderConfirmationEmailContent(String customerName, String orderId, String totalAmount) {
        return String.format("""
            Kính chào %s,
            
            Cảm ơn bạn đã đặt hàng tại Smart Shop!
            
            THÔNG TIN ĐÔN HÀNG:
            - Mã đơn hàng: #%s
            - Tổng tiền: %s VND
            - Thời gian đặt hàng: %s
            
            Đơn hàng của bạn đang được xử lý. Chúng tôi sẽ gửi thông báo cập nhật khi đơn hàng được giao.
            
            Theo dõi đơn hàng tại: %s/customer/orders
            
            Cảm ơn bạn đã tin tưởng Smart Shop!
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """, customerName, orderId, totalAmount, java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), baseUrl);
    }

    private String buildOrderStatusUpdateEmailContent(String customerName, String orderId, String newStatus) {
        String statusMessage = switch (newStatus.toLowerCase()) {
            case "processing" -> "đang được xử lý";
            case "shipped" -> "đã được giao cho đơn vị vận chuyển";
            case "delivered" -> "đã được giao thành công";
            case "cancelled" -> "đã được hủy";
            default -> newStatus;
        };

        return String.format("""
            Kính chào %s,
            
            Đơn hàng #%s của bạn %s.
            
            Bạn có thể theo dõi chi tiết đơn hàng tại: %s/customer/orders
            
            Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi.
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """, customerName, orderId, statusMessage, baseUrl);
    }

    private String buildPromotionEmailContent(String customerName, String promotionTitle, String promotionDetails, String discountCode) {
        return String.format("""
            Kính chào %s,
            
            🎉 %s 🎉
            
            %s
            
            MÃ GIẢM GIÁ: %s
            
            ⏰ Ưu đãi có hạn - Đừng bỏ lỡ cơ hội!
            
            Mua sắm ngay tại: %s
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """, customerName, promotionTitle, promotionDetails, discountCode, baseUrl);
    }

    private String buildProductRestockEmailContent(String customerName, String productName, String productUrl) {
        return String.format("""
            Kính chào %s,
            
            🎉 Tin tốt! Sản phẩm "%s" mà bạn quan tâm đã có hàng trở lại!
            
            Đừng để bỏ lỡ cơ hội này. Số lượng có hạn!
            
            Xem sản phẩm ngay: %s
            
            Cảm ơn bạn đã theo dõi Smart Shop!
            
            Trân trọng,
            Đội ngũ Smart Shop
            Email hỗ trợ: smartshopforeveryone@gmail.com
            """, customerName, productName, productUrl);
    }
}

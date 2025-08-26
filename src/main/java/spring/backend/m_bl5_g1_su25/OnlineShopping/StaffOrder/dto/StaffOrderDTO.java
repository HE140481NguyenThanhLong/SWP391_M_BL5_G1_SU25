package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.dto;

import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class StaffOrderDTO {

    private Integer orderId;
    private String orderCode;
    private String status;
    private Double total;
    private String totalFormatted;
    private String shippingAddress;
    private String createdAt;
    private String updatedAt;
    private String paymentMethod;
    private Set<OrderDetail> orderDetails; // dùng cho trang detail

    public StaffOrderDTO(Order order) {
        this.orderId = order.getOrder_id();
        this.orderCode = generateOrderCode(order.getOrder_id());
        this.status = order.getStatus().name();
        this.total = order.getTotal();
        this.totalFormatted = formatVND(order.getTotal());
        this.shippingAddress = order.getShippingAddress();
        this.createdAt = order.getCreatedAt() != null ?
                order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
        this.updatedAt = order.getUpdatedAt() != null ?
                order.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
        this.paymentMethod = order.getPaymentMethod() != null ? order.getPaymentMethod().getPaymentType().name() : "";
        this.orderDetails = order.getOrderDetails();
    }

    // --- Getter ---
    public Integer getOrderId() { return orderId; }
    public String getOrderCode() { return orderCode; }
    public String getStatus() { return status; }
    public Double getTotal() { return total; }
    public String getTotalFormatted() { return totalFormatted; }
    public String getShippingAddress() { return shippingAddress; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getPaymentMethod() { return paymentMethod; }
    public Set<OrderDetail> getOrderDetails() { return orderDetails; }

    // --- Helper methods ---
    private String generateOrderCode(Integer orderId) {
        int seed = (orderId * 9973) % 100000000; // 8 chữ số
        return String.format("ORD-%08d", seed);
    }

    private String formatVND(Double amount) {
        if (amount == null) return "";
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return nf.format(amount);
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.service;

import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.repository.StaffOrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StaffOrderService {
    private final StaffOrderRepository staffOrderRepository;

    public StaffOrderService(StaffOrderRepository staffOrderRepository) {
        this.staffOrderRepository = staffOrderRepository;
    }

    // Lấy toàn bộ order
    public List<Order> getAllOrders() {
        return staffOrderRepository.findAll();
    }

    // Lấy chi tiết order theo id
    public Optional<Order> getOrderById(Integer orderId) {
        return staffOrderRepository.findById(orderId);
    }

    // Cập nhật trạng thái order
    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = staffOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        staffOrderRepository.save(order);
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;

@Repository
public interface StaffOrderRepository extends JpaRepository<Order, Integer> {
    // Nếu cần custom query cho Staff (VD: tìm theo status) có thể thêm tại đây
}
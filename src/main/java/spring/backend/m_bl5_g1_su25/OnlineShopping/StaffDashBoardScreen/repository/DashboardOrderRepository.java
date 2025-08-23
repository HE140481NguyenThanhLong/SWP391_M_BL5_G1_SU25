package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;

@Repository
public interface DashboardOrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT COUNT(o) FROM Order o WHERE CAST(o.createdAt AS date) = CAST(CURRENT_TIMESTAMP AS date)")
    long countOrdersToday();
}

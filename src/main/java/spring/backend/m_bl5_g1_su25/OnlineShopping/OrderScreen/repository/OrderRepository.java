package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("""
        SELECT o FROM Order o
        WHERE (:status IS NULL OR o.status = :status)
          AND (:startDate IS NULL OR o.createdAt >= :startDate)
          AND (:endDate IS NULL OR o.createdAt <= :endDate)
    """)
    Page<Order> findOrdersByFilters(OrderStatus status,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    Pageable pageable);
}

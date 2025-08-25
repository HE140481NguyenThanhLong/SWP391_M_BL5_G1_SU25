package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("""
    SELECT odd FROM OrderDetail odd
    WHERE odd.order.order_id = :orderId
        """)
    List<OrderDetail> findOrderDetailByOrder_id(@Param("orderId") Integer orderId);
}

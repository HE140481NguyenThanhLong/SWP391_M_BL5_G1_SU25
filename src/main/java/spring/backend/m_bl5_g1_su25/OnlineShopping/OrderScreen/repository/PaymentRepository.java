package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("""
        SELECT p FROM Payment p
        JOIN Order o ON o.paymentMethod.payment_id = p.payment_id
        WHERE o.order_id = :orderId
    """)
    Optional<Payment> findByOrderId(int orderId);
}

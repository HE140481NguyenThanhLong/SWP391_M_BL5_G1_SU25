package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;

@Repository
public interface PaymentRepository2 extends JpaRepository<Payment, Long> {
}

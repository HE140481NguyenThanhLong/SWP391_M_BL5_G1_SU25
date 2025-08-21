package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.TransactionStatus;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Table(name="[Transaction]")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer trans_id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    Payment paymentMethod;

    @Column(precision = 18, scale = 2)  // amount of money
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    TransactionStatus status = TransactionStatus.PENDING;

    @Column(columnDefinition = "NVARCHAR(255)")
    String verificationCode;

}

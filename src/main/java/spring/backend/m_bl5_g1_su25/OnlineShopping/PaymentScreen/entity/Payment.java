package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.PaymentType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Table(name="Payment")

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer payment_id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PaymentType paymentType = PaymentType.CASH_ON_DELIVERY;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}

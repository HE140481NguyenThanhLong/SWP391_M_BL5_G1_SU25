package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer voucher_id;

    @Column(length = 50, nullable = false, unique = true)
    String code;

    @Column(nullable = false, precision = 18, scale = 2)
    BigDecimal discountAmount;

    @Column(nullable = false)
    LocalDateTime expirationDate;
    @Column(nullable = false)
    Boolean isUsed = false;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    User user;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime createdAt;
}

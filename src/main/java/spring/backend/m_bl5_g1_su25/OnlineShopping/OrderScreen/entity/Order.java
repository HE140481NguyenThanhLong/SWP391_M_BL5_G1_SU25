package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="[Order]")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer order_id;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id",nullable = false)
    User user;
//    @Column(columnDefinition = "DOUBLE",nullable = false)
//    Double total;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    OrderStatus status = OrderStatus.PENDING;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    Payment paymentMethod;
    @Column(columnDefinition = "NVARCHAR(255)",nullable = false)
    String shippingAddress;
    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails = new HashSet<>();

}

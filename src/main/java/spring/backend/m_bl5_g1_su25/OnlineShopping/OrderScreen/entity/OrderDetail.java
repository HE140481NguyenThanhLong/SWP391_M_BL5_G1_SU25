package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.math.BigDecimal;

@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="OrderDetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderDetailId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
    @Column(precision = 18, scale = 2)
    BigDecimal totalPrice;
    @Column()
    Integer quantity;

}

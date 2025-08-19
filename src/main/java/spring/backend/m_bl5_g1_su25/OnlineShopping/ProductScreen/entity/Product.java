package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name ="Product")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer product_id;

    @Column(nullable = false,length = 255,columnDefinition = "NVARCHAR(255)")
    String name;
    @Column(nullable = false,length = 255,columnDefinition = "NVARCHAR(255)")
    String description;
    @Column(nullable = false)
    Double price;
    @Column()
    Integer quantity;
    @Column(nullable = false)
    Integer sales_count=0;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    Status status = Status.IN_STOCK;
    @Column(columnDefinition = "NVARCHAR(255)")
    String imageUrl;
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column()
    LocalDateTime updatedAt;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();



}
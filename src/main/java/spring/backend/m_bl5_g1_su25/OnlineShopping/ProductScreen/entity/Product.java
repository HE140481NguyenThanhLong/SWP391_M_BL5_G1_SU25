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
    @Column(length = 300)
    String instruc;
    @Column(length = 300)
    String feature ;
    @Column(length = 300)
    String expiry ;
    @Column(length = 300)
    String origin;
    @Column(nullable = false, precision = 18, scale = 2)
    BigDecimal price;
    @Column()
    String supplier;
    @Column()
    Integer quantity;
    @Column(nullable = false)
    Integer salesCount=0;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    Status status = Status.IN_STOCK;
    @Column(columnDefinition = "NVARCHAR(255)")
    String imageUrl;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    @Column(nullable = false, precision = 18, scale = 2)
    BigDecimal importPrice;

    @Column(nullable = false, precision = 18, scale = 2)
    BigDecimal salePrice;
    private String brand;
    private double rating;
    private int discount;
    private int stock;
    private String sku;
    private int reviewCount;  // thêm
    private int soldCount;


}
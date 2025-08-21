package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.SupplierStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer supplier_id;

    @Column(nullable = false, length = 255, columnDefinition = "NVARCHAR(255)")
    String name;

    @Column(length = 255)
    String email;

    @Column(length = 20)
    String phone;

    @Column(length = 500, columnDefinition = "NVARCHAR(500)")
    String address;
    @Column(length = 500, columnDefinition = "NVARCHAR(500)")
    String region;

    @Column(length = 100)
    String productType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    SupplierStatus status = SupplierStatus.ACTIVE;

    @Column(columnDefinition = "NVARCHAR(1000)")
    String notes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}

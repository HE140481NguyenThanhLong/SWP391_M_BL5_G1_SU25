package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}

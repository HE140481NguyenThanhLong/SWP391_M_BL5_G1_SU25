package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer category_id;
    @Column(length = 100,nullable = false)
    String name;
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
}

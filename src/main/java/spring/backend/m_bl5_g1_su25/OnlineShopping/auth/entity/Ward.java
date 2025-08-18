package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ward")
public class Ward {
    @Id
    @Column( length = 20)
    private String ward_code; // Ví dụ: "00001" cho Phường Phúc Xá

    @Column(nullable = false)
    private String name; // Ví dụ: "Phường Phúc Xá"

    // Nhiều xã/phường thuộc về một huyện
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_code", nullable = false)
    private Province provinces;
}

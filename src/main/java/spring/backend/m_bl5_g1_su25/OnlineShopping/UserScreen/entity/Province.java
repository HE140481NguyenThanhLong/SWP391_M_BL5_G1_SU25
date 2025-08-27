package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "provinces")
public class Province {
    @Id
    @Column( length = 20)
    private String province_code; // Ví dụ: "01" cho Hà Nội, dùng mã của GSO

    @Column(columnDefinition = "NVARCHAR(255)")
    private String name; // Ví dụ: "Thành phố Hà Nội"

    // Một tỉnh có nhiều huyện
    @OneToMany(mappedBy = "provinces", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ward> wards;
}
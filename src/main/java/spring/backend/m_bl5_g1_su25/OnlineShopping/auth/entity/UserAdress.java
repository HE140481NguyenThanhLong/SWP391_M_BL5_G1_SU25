package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_address")
public class UserAdress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer user_address_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String recipientName;

    @Column
    private String phoneNumber;

    // Đây chính là phần "tổ dân phố", số nhà, tên đường
    @Column(name = "street_address")
    private String streetAddress;

    // Liên kết tới Phường/Xã cụ thể
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_code")
    private Ward ward;
}

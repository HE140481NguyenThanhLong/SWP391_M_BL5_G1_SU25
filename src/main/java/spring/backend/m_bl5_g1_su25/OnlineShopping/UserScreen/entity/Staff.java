package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Staff")
public class Staff {
    @Id
    Integer staff_id;
    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name="staff_id")
    User user;
    @Column(nullable = false,length = 50)
    String firstname;
    @Column(nullable = false,length = 50)
    String lastname;
    @Column(nullable = false,length = 50)
    String phoneNumber;

}

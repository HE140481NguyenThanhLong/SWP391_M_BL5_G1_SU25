package spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


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
    @Column(nullable = false,length = 50,columnDefinition = "NVARCHAR(255)")
    String firstname;
    @Column(nullable = false,length = 50,columnDefinition = "NVARCHAR(255)")
    String lastname;
    @Column(nullable = false,length = 50)
    String phoneNumber;
}

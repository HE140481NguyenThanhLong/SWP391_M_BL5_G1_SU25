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
@Table(name="Customer")
public class Customer {
    @Id
    Integer customer_id;
    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name="customer_id")
    User user;
    @Column(columnDefinition = "NVARCHAR(255)",nullable = false,length = 50)
    String firstname;
    @Column(nullable = false,length = 50,columnDefinition = "NVARCHAR(255)")
    String middlename;
    @Column(nullable = false,length = 50,columnDefinition = "NVARCHAR(255)")
    String lastname;


}

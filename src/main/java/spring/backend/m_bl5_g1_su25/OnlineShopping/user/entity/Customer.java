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
@Table(name="Customer")
public class Customer {
    @Id
    Integer customer_id;
    @Column(columnDefinition = "NVARCHAR(255)",nullable = false,length = 50)
    String firstname;
    @Column(nullable = false,length = 50,columnDefinition = "NVARCHAR(255)")
    String lastname;
    @Column(nullable = false,length = 50)
    String phoneNumber;
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;


import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer user_id;
    @Column(nullable = false, unique = true, length = 50)
    String username;
    @Column(nullable = false, unique = true, length = 100)
    String email;
    @Column(nullable = false, length = 255)
    String password;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime updatedAt;
    @Column(nullable = false)
    Boolean isDeleted = false;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    Role role = Role.CUSTOMER;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    UserStatus status = UserStatus.ACTIVE;

    @Column(nullable = false,length = 50)
    String phoneNumber;

}

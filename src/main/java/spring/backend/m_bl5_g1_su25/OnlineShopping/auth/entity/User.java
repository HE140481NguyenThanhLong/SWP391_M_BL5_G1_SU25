package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.Enum.Gender;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.Enum.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.Enum.Status;

import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    Role role = Role.CUSTOMER;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    Gender gender =Gender.MALE;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    LocalDateTime updatedAt;
    @Column(nullable = false)
    Boolean isDeleted = false;

    @Column(nullable = false,length = 50)
    String phoneNumber;

}

package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, length = 100)
    String email;

    @Column(nullable = false, unique = true)
    String token;

    @Column(nullable = false)
    LocalDateTime expiryTime;

    @Column(nullable = false)
    Boolean used = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    // Helper method to check if token is expired
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }

    // Helper method to check if token is valid (not used and not expired)
    public boolean isValid() {
        return !used && !isExpired();
    }

    // Generate unique token
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

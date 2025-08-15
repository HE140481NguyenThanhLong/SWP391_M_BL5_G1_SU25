package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers",
        indexes = {
                @Index(name = "idx_voucher_code", columnList = "code"),
                @Index(name = "idx_voucher_user", columnList = "user_id"),
                @Index(name = "idx_voucher_expiration", columnList = "expiration_date")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Voucher code is required")
    @Size(min = 3, max = 50, message = "Voucher code must be between 3 and 50 characters")
    private String code;

    @Column(name = "discount_amount", nullable = false, precision = 18, scale = 2)
    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0.01", message = "Discount amount must be greater than 0")
    private BigDecimal discountAmount;

    @Column(name = "expiration_date", nullable = false)
    @NotNull(message = "Expiration date is required")
    private LocalDateTime expirationDate;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

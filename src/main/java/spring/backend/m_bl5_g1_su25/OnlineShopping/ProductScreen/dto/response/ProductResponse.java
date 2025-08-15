package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    Integer product_id;
    String name;
    String description;
    String imageUrl;

    BigDecimal price;
    Integer quantity;
    Integer sales_count = 0;
    @Enumerated(EnumType.STRING)
    Status status;
    LocalDateTime created_at;
    LocalDateTime updated_at;

}

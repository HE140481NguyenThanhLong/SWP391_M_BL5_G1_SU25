package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopSellingProductDto {
    Integer productId;
    String name;
    BigDecimal price;
    Integer salesCount;
    String imageUrl;
}

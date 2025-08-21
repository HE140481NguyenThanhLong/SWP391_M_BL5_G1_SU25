package spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryProductDto {
    Integer productId;
    String name;
    String description;
    Integer quantity;
    BigDecimal price;
    BigDecimal importPrice;
    BigDecimal salePrice;
    Integer soldCount;
    String imageUrl;
}

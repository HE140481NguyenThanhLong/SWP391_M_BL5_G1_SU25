package spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryStatisticsDto {
    Long totalProducts;
    Long lowStockProducts;
    Long outOfStockProducts;
    List<InventoryProductDto> inventoryProducts;
}

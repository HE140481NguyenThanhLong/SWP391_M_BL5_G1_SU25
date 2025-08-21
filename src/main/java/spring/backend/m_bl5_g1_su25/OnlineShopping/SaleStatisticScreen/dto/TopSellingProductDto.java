package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopSellingProductDto {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private Integer salesCount;
    private String imageUrl;
}

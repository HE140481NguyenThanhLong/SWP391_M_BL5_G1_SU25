package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesStatisticsDto {
    Double totalRevenue;
    Integer totalOrders;
    Integer totalProductsSold;
    Double averageOrderValue;
    List<TopSellingProductDto> topSellingProducts;
}

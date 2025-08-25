package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesStatisticsDto {
    private Double totalRevenue;
    private Integer totalOrders;
    private Integer totalProductsSold;
    private Double averageOrderValue;
    private List<TopSellingProductDto> topSellingProducts;
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataDTO {
    private long ordersToday;
    private long totalProducts;
    private long lowStockProducts;
    private long outOfStockProducts;
}

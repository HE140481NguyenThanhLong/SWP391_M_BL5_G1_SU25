package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.dto.DashboardDataDTO;

@Service
@RequiredArgsConstructor
public class StaffDashboardService {

    private final DashboardOrderRepository orderRepository;
    private final DashboardProductRepository productRepository;

    public DashboardDataDTO getDashboardData() {
        // Đếm đơn hàng hôm nay
        long ordersToday = orderRepository.countOrdersToday();
        
        // Đếm tổng sản phẩm
        long totalProducts = productRepository.countAllProducts();
        
        // Đếm sản phẩm sắp hết hàng (≤ 10 và > 0)
        long lowStockProducts = productRepository.countLowStockProducts();
        
        // Đếm sản phẩm hết hàng (= 0 hoặc OUT_STOCK)
        long outOfStockProducts = productRepository.countOutOfStockProducts();
        
        return DashboardDataDTO.builder()
                .ordersToday(ordersToday)
                .totalProducts(totalProducts)
                .lowStockProducts(lowStockProducts)
                .outOfStockProducts(outOfStockProducts)
                .build();
    }
}

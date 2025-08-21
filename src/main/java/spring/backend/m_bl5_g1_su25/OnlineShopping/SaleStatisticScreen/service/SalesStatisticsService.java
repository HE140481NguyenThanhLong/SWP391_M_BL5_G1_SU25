package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.SalesStatisticsDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.TopSellingProductDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesStatisticsService {

    EntityManager entityManager;

    public SalesStatisticsDto getSalesStatistics() {
        return SalesStatisticsDto.builder()
                .totalRevenue(getTotalRevenue())
                .totalOrders(getTotalOrders())
                .totalProductsSold(getTotalProductsSold())
                .averageOrderValue(getAverageOrderValue())
                .topSellingProducts(getTopSellingProducts())
                .build();
    }

    private Double getTotalRevenue() {
        try {
            String sql = "SELECT COALESCE(SUM(CAST(o.total AS DECIMAL(18,2))), 0) FROM [Order] o WHERE o.status = 'DELIVERED'";
            Query query = entityManager.createNativeQuery(sql);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Integer getTotalOrders() {
        try {
            String sql = "SELECT COUNT(*) FROM [Order] o";
            Query query = entityManager.createNativeQuery(sql);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Integer getTotalProductsSold() {
        try {
            // Sử dụng tên cột thực tế trong database: sold_count
            String sql = "SELECT COALESCE(SUM(p.sold_count), 0) FROM Product p";
            Query query = entityManager.createNativeQuery(sql);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).intValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Double getAverageOrderValue() {
        try {
            String sql = "SELECT COALESCE(AVG(CAST(o.total AS DECIMAL(18,2))), 0) FROM [Order] o WHERE o.status = 'DELIVERED'";
            Query query = entityManager.createNativeQuery(sql);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private List<TopSellingProductDto> getTopSellingProducts() {
        try {
            // Sử dụng tên cột thực tế trong database: sold_count và image_url
            String sql = "SELECT TOP 5 p.product_id, p.name, " +
                        "COALESCE(CAST(p.price AS DECIMAL(18,2)), 0) as price, " +
                        "COALESCE(p.sold_count, 0) as sold_count, " +
                        "p.image_url " +
                        "FROM Product p " +
                        "WHERE p.sold_count > 0 " +
                        "ORDER BY p.sold_count DESC";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();
            List<TopSellingProductDto> topProducts = new ArrayList<>();

            for (Object[] row : results) {
                TopSellingProductDto product = TopSellingProductDto.builder()
                        .productId(row[0] != null ? ((Number) row[0]).intValue() : 0)
                        .name(row[1] != null ? (String) row[1] : "Unknown")
                        .price(row[2] != null ? BigDecimal.valueOf(((Number) row[2]).doubleValue()) : BigDecimal.ZERO)
                        .salesCount(row[3] != null ? ((Number) row[3]).intValue() : 0)
                        .imageUrl(row[4] != null ? (String) row[4] : "")
                        .build();
                topProducts.add(product);
            }

            return topProducts;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

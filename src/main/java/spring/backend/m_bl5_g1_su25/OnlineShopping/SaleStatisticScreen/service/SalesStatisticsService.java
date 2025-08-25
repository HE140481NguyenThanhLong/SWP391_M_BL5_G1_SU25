package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.SalesStatisticsDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.TopSellingProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesStatisticsService {
    private final EntityManager entityManager;
    public SalesStatisticsDto getSalesStatistics() {
    return SalesStatisticsDto.builder().totalRevenue(getTotalRevenue())
           .totalOrders(getTotalOrders()).totalProductsSold(getTotalProductsSold())
           .averageOrderValue(getAverageOrderValue()).topSellingProducts(getTopSellingProducts()).build();
    }
private Double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(CAST(total AS DECIMAL(18,2))), 0) FROM [Order]";
        Query query = entityManager.createNativeQuery(sql);
        Number result = (Number) query.getSingleResult();
        return result.doubleValue();
    }
private Integer getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM [Order]";
        Query query = entityManager.createNativeQuery(sql);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }
private Integer getTotalProductsSold() {
        String sql = "SELECT COALESCE(SUM(sold_count), 0) FROM Product";
        Query query = entityManager.createNativeQuery(sql);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }
private Double getAverageOrderValue() {
        String sql = "SELECT COALESCE(AVG(CAST(total AS DECIMAL(18,2))), 0) FROM [Order]";
        Query query = entityManager.createNativeQuery(sql);
        Number result = (Number) query.getSingleResult();
        return result.doubleValue();
    }
private List<TopSellingProductDto> getTopSellingProducts() {
    String sql = "SELECT TOP 5 product_id, name, price, sold_count, image_url " + "FROM Product " + "ORDER BY sold_count DESC";
    Query query = entityManager.createNativeQuery(sql);
    List<Object[]> results = query.getResultList();
    List<TopSellingProductDto> topProducts = new ArrayList<>();
    for (Object[] row : results) {
        TopSellingProductDto product = TopSellingProductDto.builder()
            .productId(((Number) row[0]).intValue())
            .name((String) row[1])
            .price(BigDecimal.valueOf(((Number) row[2]).doubleValue()))
            .salesCount(((Number) row[3]).intValue())
            .imageUrl((String) row[4])
            .build();
            topProducts.add(product);
        }
        return topProducts;
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.SalesStatisticsDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.service.SalesStatisticsService;

/**
 *
 *
 * Endpoints:
 * - /staff/sales-statistics: View sales statistics (STAFF only)
 */

@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
public class SalesStatisticsController {

    private final SalesStatisticsService salesStatisticsService;

    @GetMapping("/sales-statistics")
    public String getSalesStatistics(Model model) {
        SalesStatisticsDto statistics = salesStatisticsService.getSalesStatistics();
        model.addAttribute("totalRevenue", statistics.getTotalRevenue());
        model.addAttribute("totalOrders", statistics.getTotalOrders());
        model.addAttribute("totalProductsSold", statistics.getTotalProductsSold());
        model.addAttribute("averageOrderValue", statistics.getAverageOrderValue());
        model.addAttribute("topSellingProducts", statistics.getTopSellingProducts());
        return "staff/sales-statistics";
    }
}

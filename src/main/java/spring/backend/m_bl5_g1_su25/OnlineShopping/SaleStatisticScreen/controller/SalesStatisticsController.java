package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.SalesStatisticsDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.service.SalesStatisticsService;

@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesStatisticsController {

    SalesStatisticsService salesStatisticsService;

    @GetMapping("/sales-statistics")
    public String getSalesStatistics(Model model) {
        try {
            SalesStatisticsDto statistics = salesStatisticsService.getSalesStatistics();

            // Add data to model for Thymeleaf
            model.addAttribute("totalRevenue", statistics.getTotalRevenue());
            model.addAttribute("totalOrders", statistics.getTotalOrders());
            model.addAttribute("totalProductsSold", statistics.getTotalProductsSold());
            model.addAttribute("averageOrderValue", statistics.getAverageOrderValue());
            model.addAttribute("topSellingProducts", statistics.getTopSellingProducts());

        } catch (Exception e) {
            // Set default values in case of error
            model.addAttribute("totalRevenue", 0.0);
            model.addAttribute("totalOrders", 0);
            model.addAttribute("totalProductsSold", 0);
            model.addAttribute("averageOrderValue", 0.0);
            model.addAttribute("topSellingProducts", java.util.Collections.emptyList());
        }

        return "staff/sales-statistics";
    }
}

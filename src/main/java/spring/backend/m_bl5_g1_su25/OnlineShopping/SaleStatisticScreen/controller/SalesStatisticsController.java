package spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.dto.SalesStatisticsDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.SaleStatisticScreen.service.SalesStatisticsService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
/**
 * Endpoints:
 * - /staff/sales-statistics: View sales statistics (STAFF only)
 */
@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
public class SalesStatisticsController {
private final SalesStatisticsService salesStatisticsService;
@GetMapping("/sales-statistics")
public String getSalesStatistics(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");
if (loggedInUser == null)
    return "redirect:/authority/signin";

if (loggedInUser.getRole() != Role.STAFF) {
   redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập trang này");
   return "redirect:/";
}
try {
SalesStatisticsDto statistics = salesStatisticsService.getSalesStatistics();
model.addAttribute("totalRevenue", statistics.getTotalRevenue());
model.addAttribute("totalOrders", statistics.getTotalOrders());
model.addAttribute("totalProductsSold", statistics.getTotalProductsSold());
model.addAttribute("averageOrderValue", statistics.getAverageOrderValue());
model.addAttribute("topSellingProducts", statistics.getTopSellingProducts());
model.addAttribute("loggedInUser", loggedInUser);
return "staff/sales-statistics";
} catch (Exception e) {
     System.err.println("Error loading sales statistics: " + e.getMessage());
     redirectAttributes.addFlashAttribute("error", "Không thể tải dữ liệu thống kê: " + e.getMessage());
     return "redirect:/staff/staff-dashboard";
        }
    }
}

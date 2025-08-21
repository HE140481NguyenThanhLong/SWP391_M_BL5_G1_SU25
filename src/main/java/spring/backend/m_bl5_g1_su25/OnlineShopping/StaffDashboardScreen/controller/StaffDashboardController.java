package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository.DashboardOrderRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository.DashboardProductRepository;

@Controller
@RequestMapping("/staff")
public class StaffDashboardController {

    @Autowired
    private DashboardOrderRepository orderRepository;

    @Autowired
    private DashboardProductRepository productRepository;

    @GetMapping("/dashboard")
    public String staffDashboard(Model model) {
        try {
            // Lấy dữ liệu thống kê trực tiếp từ repository
            long ordersToday = orderRepository.countOrdersToday();
            long totalProducts = productRepository.countAllProducts();
            long lowStockProducts = productRepository.countLowStockProducts();
            long outOfStockProducts = productRepository.countOutOfStockProducts();

            // Thêm dữ liệu vào model
            model.addAttribute("ordersToday", ordersToday);
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("lowStockProducts", lowStockProducts);
            model.addAttribute("outOfStockProducts", outOfStockProducts);

        } catch (Exception e) {
            // Log error và sử dụng giá trị mặc định
            System.err.println("Error loading dashboard data: " + e.getMessage());
            model.addAttribute("ordersToday", 0L);
            model.addAttribute("totalProducts", 0L);
            model.addAttribute("lowStockProducts", 0L);
            model.addAttribute("outOfStockProducts", 0L);
        }

        return "staff/staff-dashboard";
    }
}

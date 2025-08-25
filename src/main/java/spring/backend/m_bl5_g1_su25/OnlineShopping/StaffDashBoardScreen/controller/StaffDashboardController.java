package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository.DashboardOrderRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository.DashboardProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;

@Controller
@RequestMapping("/staff")
public class StaffDashboardController {
@Autowired
private DashboardOrderRepository orderRepository;
@Autowired
private DashboardProductRepository productRepository;
@GetMapping("/staff-dashboard")
public String staffDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    User loggedInUser = (User) session.getAttribute("loggedInUser");
    if (loggedInUser == null) {
    return "redirect:/authority/signin";
        }
    if (loggedInUser.getRole() != Role.STAFF) {
        redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập trang này");
    return "redirect:/";
        }
try {
long ordersToday = orderRepository.countOrdersToday();
long totalProducts = productRepository.countAllProducts();
long lowStockProducts = productRepository.countLowStockProducts();
long outOfStockProducts = productRepository.countOutOfStockProducts();

model.addAttribute("ordersToday", ordersToday);
model.addAttribute("totalProducts", totalProducts);
model.addAttribute("lowStockProducts", lowStockProducts);
model.addAttribute("outOfStockProducts", outOfStockProducts);
model.addAttribute("loggedInUser", loggedInUser);

} catch (Exception e) {
     System.err.println("Error loading dashboard data: " + e.getMessage());
    }
    return "staff/staff-dashboard";
    }
}

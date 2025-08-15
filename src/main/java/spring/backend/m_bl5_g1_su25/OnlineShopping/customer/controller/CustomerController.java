package spring.backend.m_bl5_g1_su25.OnlineShopping.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.AuthService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.User;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final AuthService authService;

    @GetMapping("/dashboard")
    public String customerDashboard(Authentication authentication, Model model) {
        // Spring Security already handles authentication check via SecurityConfig
        User user = authService.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        model.addAttribute("role", user.getRole());

        return "customer/customer-dashboard";
    }

    @GetMapping("/orders")
    public String customerOrders(Authentication authentication, Model model) {
        User user = authService.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        // TODO: Add order retrieval logic
        return "customer/orders";
    }
}

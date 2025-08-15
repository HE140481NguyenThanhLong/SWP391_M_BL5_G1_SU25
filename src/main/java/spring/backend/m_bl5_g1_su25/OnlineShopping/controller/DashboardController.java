package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Simple dashboard page - no complex logic
        model.addAttribute("message", "Welcome to Small Shop Dashboard");
        return "dashboard";
    }

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        // Clear session when accessing root path
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/dashboard";
    }
}

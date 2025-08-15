package spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.dto.RegisterRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.AuthService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.User;

@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final AuthService authService;

    @GetMapping("/dashboard")
    public String staffDashboard(Authentication authentication, Model model) {
        // Spring Security already handles authentication check via SecurityConfig
        User user = authService.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        model.addAttribute("role", user.getRole());

        return "staff/staff-main-screen";
    }

    @GetMapping("/create-account")
    public String createAccountPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "staff/create-account";
    }

    @PostMapping("/create-account")
    public String createAccount(@Valid @ModelAttribute RegisterRequest registerRequest,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "staff/create-account";
        }

        try {
            authService.register(registerRequest);
            redirectAttributes.addFlashAttribute("success",
                "Account created successfully: " + registerRequest.getEmail());
            return "redirect:/staff/create-account";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/staff/create-account";
        }
    }

    @GetMapping("/orders")
    public String staffOrders(Authentication authentication, Model model) {
        User user = authService.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        // TODO: Add order management logic
        return "staff/orders";
    }
}

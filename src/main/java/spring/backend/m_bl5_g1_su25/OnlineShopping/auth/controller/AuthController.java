package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Root path - redirect to dashboard
    @GetMapping({"/", ""})
    public String home() {
        return "redirect:/dashboard";
    }

    // Login page - Spring Security handles the actual login
    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request) {
        // If already authenticated, redirect to dashboard
        if (request.getUserPrincipal() != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    // Signup page
    @GetMapping("/signup")
    public String showSignupPage(Model model, HttpServletRequest request) {
        // If already authenticated, redirect to dashboard
        if (request.getUserPrincipal() != null) {
            return "redirect:/dashboard";
        }

        model.addAttribute("registerRequest", new RegisterRequest());
        return "signup";
    }

    // Handle signup submission
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute RegisterRequest registerRequest,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "signup";
        }

        try {
            authService.register(registerRequest);
            redirectAttributes.addFlashAttribute("success",
                "Account created successfully! Please login with your credentials.");
            return "redirect:/login?registered=true";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }

    // Dashboard - hiển thị trang chung hoặc guest dashboard
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Nếu User đã authenticated, hiển thị thông tin User
        if (authentication != null && authentication.isAuthenticated() &&
            !authentication.getName().equals("anonymousUser")) {

            try {
                String email = authentication.getName();
                System.out.println("DEBUG: Authenticated user email: " + email);

                User user = authService.findByEmail(email);
                System.out.println("DEBUG: Found user: " + user.getUsername() + " with role: " + user.getRole());

                model.addAttribute("isGuest", false);
                model.addAttribute("username", user.getUsername());
                model.addAttribute("role", user.getRole().toString());
                model.addAttribute("email", user.getEmail());
                return "dashboard";
            } catch (Exception e) {
                // Log the error for debugging
                System.err.println("ERROR: Failed to load user for email: " + authentication.getName());
                System.err.println("ERROR: " + e.getMessage());
                e.printStackTrace();

                // Force logout if user lookup fails
                return "redirect:/logout";
            }
        }

        // Hiển thị guest dashboard
        model.addAttribute("isGuest", true);
        model.addAttribute("username", "Guest");
        model.addAttribute("role", "GUEST");
        return "dashboard";
    }
}

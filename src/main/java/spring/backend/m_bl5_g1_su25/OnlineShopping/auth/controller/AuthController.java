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

    // Root path - redirect to guest home (with products)
    @GetMapping({"/", ""})
    public String home() {
        return "redirect:/guest";
    }

    // Login page - Spring Security handles the actual login
    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request) {
        // If already authenticated, redirect to guest home
        if (request.getUserPrincipal() != null) {
            return "redirect:/guest";
        }
        return "login";
    }

    // Signup page
    @GetMapping("/signup")
    public String showSignupPage(Model model, HttpServletRequest request) {
        // If already authenticated, redirect to guest home
        if (request.getUserPrincipal() != null) {
            return "redirect:/guest";
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


}

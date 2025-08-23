package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.AuthorizedService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.PasswordResetService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthorizedController {
    AuthorizedService authorizedService;
    PasswordResetService passwordResetService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute SignUpRequest request, Model model) {
        try{
            Customer signUpCus = authorizedService.signUp(request);
            return "redirect:/auth/login?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/signup";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        try {
            boolean success = passwordResetService.sendPasswordResetEmail(email);
            if (success) {
                return "redirect:/auth/forgot-password?sent=true";
            } else {
                return "redirect:/auth/forgot-password?error=true";
            }
        } catch (Exception e) {
            return "redirect:/auth/forgot-password?error=true";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        boolean isValidToken = passwordResetService.isValidToken(token);
        User user = passwordResetService.getUserByToken(token);

        model.addAttribute("validToken", isValidToken);
        model.addAttribute("token", token);

        if (isValidToken && user != null) {
            model.addAttribute("username", user.getUsername());
        }

        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               HttpSession session) {

        if (!password.equals(confirmPassword)) {
            return "redirect:/auth/reset-password?token=" + token;
        }

        try {
            boolean success = passwordResetService.resetPassword(token, password);
            if (success) {
                session.invalidate();
                return "redirect:/auth/login?reset=success";
            } else {
                return "redirect:/auth/reset-password?token=" + token;
            }
        } catch (Exception e) {
            return "redirect:/auth/reset-password?token=" + token;
        }
    }
}

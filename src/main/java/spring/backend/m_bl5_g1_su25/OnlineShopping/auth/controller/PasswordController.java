package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.dto.ForgotPasswordRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.dto.ResetPasswordRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.AuthService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.PasswordResetService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.service.MockEmailService;

@Controller
@RequestMapping("/shared/change-password")
@RequiredArgsConstructor
@Slf4j
public class PasswordController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final MockEmailService emailService;

    // Show change password page
    @GetMapping
    public String showChangePasswordPage(Model model, @RequestParam(required = false) String token) {
        if (token != null && !token.trim().isEmpty()) {
            // User clicked reset link - validate token and show password reset form
            try {
                String email = passwordResetService.validateResetToken(token);

                ResetPasswordRequest resetRequest = new ResetPasswordRequest();
                resetRequest.setToken(token);

                model.addAttribute("resetPasswordRequest", resetRequest);
                model.addAttribute("resetToken", token);
                model.addAttribute("email", email);

            } catch (Exception e) {
                log.error("Invalid or expired reset token: {}", token);
                model.addAttribute("error", "Invalid or expired reset link. Please request a new one.");
                // Always provide forgotPasswordRequest for fallback
                model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
            }
        } else {
            // Normal access - show email input form
            model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        }

        // Always ensure these objects exist to prevent Thymeleaf errors
        if (!model.containsAttribute("forgotPasswordRequest")) {
            model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        }
        if (!model.containsAttribute("resetPasswordRequest")) {
            model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());
        }

        return "shared/change-password";
    }

    // Step 1: Send reset link to email
    @PostMapping("/send-reset-link")
    public String sendResetLink(@Valid @ModelAttribute ForgotPasswordRequest forgotPasswordRequest,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please provide a valid email address");
            return "redirect:/shared/change-password";
        }

        try {
            String email = forgotPasswordRequest.getEmail().trim().toLowerCase();

            // Check if user exists with this email
            if (!authService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "No account found with this email address");
                return "redirect:/shared/change-password";
            }

            // Generate token and send reset email
            passwordResetService.sendPasswordResetEmail(email);

            // Show success message
            redirectAttributes.addFlashAttribute("success",
                "A password reset link has been sent to your email address");
            redirectAttributes.addFlashAttribute("tokenSent", true);

            return "redirect:/shared/change-password";

        } catch (Exception e) {
            log.error("Error sending password reset email for: {}. Error: {}",
                forgotPasswordRequest.getEmail(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to send reset email. Please try again later.");
            return "redirect:/shared/change-password";
        }
    }

    // Step 2: Reset password with token
    @PostMapping("/reset")
    public String resetPassword(@Valid @ModelAttribute ResetPasswordRequest resetRequest,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        try {
            // Validate form data
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("error", "Please fill all required fields correctly");
                return "redirect:/shared/change-password?token=" + resetRequest.getToken();
            }

            // Check if passwords match
            if (!resetRequest.isPasswordsMatch()) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match");
                return "redirect:/shared/change-password?token=" + resetRequest.getToken();
            }

            // Validate token again (double-check)
            String email = passwordResetService.validateResetToken(resetRequest.getToken());

            // Validate password requirements
            authService.validatePasswordChangeRequest(email, resetRequest.getNewPassword(), resetRequest.getConfirmPassword());

            // Reset password
            authService.changePassword(email, resetRequest.getNewPassword());

            // Mark token as used to prevent reuse
            passwordResetService.markTokenAsUsed(resetRequest.getToken());

            // Send notification email (non-blocking)
            try {
                emailService.sendPasswordChangeNotification(email);
            } catch (Exception e) {
                log.warn("Failed to send password change notification to: {}", email);
            }

            // Success
            redirectAttributes.addFlashAttribute("success",
                "Password reset successfully! You can now login with your new password.");
            return "redirect:/login";

        } catch (Exception e) {
            log.error("Error resetting password for token: {}. Error: {}",
                resetRequest.getToken(), e.getMessage(), e);

            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/shared/change-password?token=" + resetRequest.getToken();
        }
    }
}

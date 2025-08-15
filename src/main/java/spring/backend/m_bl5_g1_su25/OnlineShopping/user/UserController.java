package spring.backend.m_bl5_g1_su25.OnlineShopping.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.AuthService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping({"/customer/profile", "/staff/profile"})
    public String showProfile(Authentication authentication, Model model) {
        User user = authService.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("address", user.getAddress());

        return "shared/profile";
    }

    @PostMapping({"/customer/profile/update", "/staff/profile/update"})
    public String updateProfile(@RequestParam("name") String name,
                               @RequestParam("email") String email,
                               @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("address") String address,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // Validation thủ công cho các field cần thiết
        StringBuilder errors = new StringBuilder();

        if (name == null || name.trim().length() < 2 || name.trim().length() > 100) {
            errors.append("Name must be between 2 and 100 characters; ");
        }

        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Please provide a valid email address; ");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            errors.append("Phone number is required; ");
        }

        if (address == null || address.trim().isEmpty()) {
            errors.append("Address is required; ");
        }

        if (errors.length() > 0) {
            // Có lỗi validation - hiển thị lại form với lỗi
            User currentUser = authService.findByEmail(authentication.getName());

            model.addAttribute("user", currentUser);
            model.addAttribute("username", currentUser.getName());
            model.addAttribute("email", currentUser.getEmail());
            model.addAttribute("phoneNumber", currentUser.getPhoneNumber());
            model.addAttribute("address", currentUser.getAddress());
            model.addAttribute("error", "Please fix the following errors: " + errors.toString());

            return "shared/profile";
        }

        try {
            // Lấy user hiện tại từ database
            User currentUser = authService.findByEmail(authentication.getName());

            // Check if email đã thay đổi và có tồn tại chưa
            if (!currentUser.getEmail().equals(email.trim()) &&
                authService.existsByEmail(email.trim())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists");
                return getRedirectPath(authentication);
            }

            // Chỉ cập nhật 4 trường này, không động vào password và role
            currentUser.setName(name.trim());
            currentUser.setEmail(email.trim());
            currentUser.setPhoneNumber(phoneNumber.trim());
            currentUser.setAddress(address.trim());

            userRepository.save(currentUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");

            return getRedirectPath(authentication);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return getRedirectPath(authentication);
        }
    }

    private String getRedirectPath(Authentication authentication) {
        User user = authService.findByEmail(authentication.getName());
        return switch (user.getRole()) {
            case CUSTOMER -> "redirect:/customer/profile";
            case STAFF -> "redirect:/staff/profile";
            default -> "redirect:/dashboard";
        };
    }
}

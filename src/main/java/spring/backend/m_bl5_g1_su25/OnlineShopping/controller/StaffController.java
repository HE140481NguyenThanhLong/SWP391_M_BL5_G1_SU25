package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.service.UserService;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String staffDashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("role", "STAFF");
        return "staff/staff-main-screen";
    }

    @GetMapping("/create-account")
    public String createAccountPage(Model model) {
        return "staff/create-account";
    }

    @PostMapping("/create-account")
    public String createAccount(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String phoneNumber,
                               @RequestParam String address,
                               @RequestParam String password,
                               @RequestParam String role,
                               RedirectAttributes redirectAttributes) {
        try {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setAddress(address);
            newUser.setPassword(password); // Will be encoded in UserService

            // Set role based on selection
            switch (role.toUpperCase()) {
                case "STAFF":
                    newUser.setRole(User.UserRole.STAFF);
                    break;
                case "DELIVERER":
                    newUser.setRole(User.UserRole.DELIVERER);
                    break;
                case "CUSTOMER":
                default:
                    newUser.setRole(User.UserRole.CUSTOMER);
                    break;
            }

            userService.registerUser(newUser);
            redirectAttributes.addFlashAttribute("success",
                "Account created successfully for " + name + " with role " + role + ". Email: " + email + ", Password: " + password);

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/staff/create-account";
    }

    // Thêm endpoint để tạo tài khoản admin đầu tiên
    @GetMapping("/create-initial-admin")
    public String createInitialAdmin(RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra xem đã có admin chưa
            if (userService.findByEmail("admin@smartshop.com").isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Admin account already exists!");
                return "redirect:/staff/dashboard";
            }

            User adminUser = new User();
            adminUser.setName("System Admin");
            adminUser.setEmail("admin@smartshop.com");
            adminUser.setPhoneNumber("0123456789");
            adminUser.setAddress("Smart Shop HQ");
            adminUser.setPassword("admin123");
            adminUser.setRole(User.UserRole.STAFF);

            userService.registerUser(adminUser);
            redirectAttributes.addFlashAttribute("success",
                "Initial admin account created! Email: admin@smartshop.com, Password: admin123");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/staff/dashboard";
    }
}

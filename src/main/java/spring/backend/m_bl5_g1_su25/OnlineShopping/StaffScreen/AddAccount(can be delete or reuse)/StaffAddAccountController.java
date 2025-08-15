package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/dashboard")
    public String staffDashboard(Authentication authentication, Model model) {
        // Get user from database to get actual name
        userService.findByEmail(authentication.getName())
                .ifPresent(user -> model.addAttribute("name", user.getName()));
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

            // Set role based on selection (only STAFF and CUSTOMER allowed)
            User.UserRole userRole = switch (role.toUpperCase()) {
                case "STAFF" -> User.UserRole.STAFF;
                case "CUSTOMER" -> User.UserRole.CUSTOMER;
                default -> User.UserRole.CUSTOMER;
            };
            newUser.setRole(userRole);

            userService.registerUser(newUser);
            redirectAttributes.addFlashAttribute("success",
                "Account created successfully for " + name + " with role " + role);

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/staff/create-account";
    }
}

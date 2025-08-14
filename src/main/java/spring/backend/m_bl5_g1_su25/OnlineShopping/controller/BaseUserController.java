package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.service.UserService;

public abstract class BaseUserController {

    @Autowired
    protected UserService userService;

    /**
     * Abstract method to get the role name for this controller
     */
    protected abstract String getRoleName();

    /**
     * Abstract method to get the dashboard template path
     */
    protected abstract String getDashboardTemplate();

    /**
     * Abstract method to get the profile redirect URL
     */
    protected abstract String getProfileRedirectUrl();

    /**
     * Common dashboard logic - to be called from concrete controller methods
     */
    protected String handleDashboard(Authentication authentication, Model model) {
        // Get user from database to get actual name
        userService.findByEmail(authentication.getName())
                .ifPresent(user -> model.addAttribute("name", user.getName()));
        model.addAttribute("role", getRoleName());
        return getDashboardTemplate();
    }

    /**
     * Common profile viewing logic - to be called from concrete controller methods
     */
    protected String handleProfile(Authentication authentication, Model model) {
        userService.findByEmail(authentication.getName())
                .ifPresent(user -> model.addAttribute("user", user));
        return "shared/profile";
    }

    /**
     * Common profile update logic - to be called from concrete controller methods
     */
    protected String handleUpdateProfile(User user, Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<User> existingUserOpt = userService.findByEmail(authentication.getName());
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                existingUser.setName(user.getName());
                existingUser.setAddress(user.getAddress());
                existingUser.setPhoneNumber(user.getPhoneNumber());
                // Don't update email and password here for security

                userService.save(existingUser);
                redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }
        return "redirect:" + getProfileRedirectUrl();
    }
}

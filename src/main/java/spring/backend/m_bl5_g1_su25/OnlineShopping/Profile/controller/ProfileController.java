package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.service.ProfileService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.ProfileViewDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;

import java.util.Optional;

/**
 * Các chức năng quản lý hồ sơ người dùng: staff và customer tự xem bản thân
 *admin thì xem của người khác
 * Endpoints:
 * - /profile/view: View own profile (STAFF, CUSTOMER, ADMIN)
 * - /profile/admin/{userId}: Admin view any user's profile (ADMIN only)
 * - /profile/edit: Edit own profile
 * - /profile/admin/{userId}/edit: Admin edit any user's profile? (ADMIN only)(idk, not merged yet)
 */
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuthorizedRepo authorizedRepo;

    @GetMapping("/view")
    public String viewOwnProfile(Model model) {
        User user = getCurrentUser();
        ProfileViewDto profile = profileService.getProfileView(user);
        model.addAttribute("profile", profile);
        model.addAttribute("isOwnProfile", true);
        return "profile/ViewProfile";
    }

    @GetMapping("/admin/{userId}")
    public String viewUserProfile(@PathVariable Integer userId, Model model) {
        User currentUser = getCurrentUser();

        if (!hasRole(currentUser, Role.ADMIN)) {
            return "redirect:/dashboard";
        }

        Optional<User> targetUserOpt = authorizedRepo.findById(userId);
        if (targetUserOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "redirect:/admin/dashboard";
        }

        ProfileViewDto profile = profileService.getProfileView(targetUserOpt.get());
        model.addAttribute("profile", profile);
        model.addAttribute("isAdminView", true);
        model.addAttribute("targetUserId", userId);
        return "profile/ViewProfile";
    }

    @GetMapping("/edit")
    public String editOwnProfile(Model model) {
        User user = getCurrentUser();
        ProfileViewDto profile = profileService.getProfileView(user);
        model.addAttribute("profile", profile);
        model.addAttribute("isOwnProfile", true);
        return "profile/EditProfile";
    }

    @GetMapping("/admin/{userId}/edit")
    public String editUserProfile(@PathVariable Integer userId, Model model) {
        User currentUser = getCurrentUser();

        if (!hasRole(currentUser, Role.ADMIN)) {
            return "redirect:/dashboard";
        }

        Optional<User> targetUserOpt = authorizedRepo.findById(userId);
        if (targetUserOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "redirect:/admin/dashboard";
        }

        ProfileViewDto profile = profileService.getProfileView(targetUserOpt.get());
        model.addAttribute("profile", profile);
        model.addAttribute("isAdminEdit", true);
        model.addAttribute("targetUserId", userId);
        return "profile/EditProfile";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return authorizedRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private boolean hasRole(User user, Role role) {
        return user.getRole() == role;
    }
}

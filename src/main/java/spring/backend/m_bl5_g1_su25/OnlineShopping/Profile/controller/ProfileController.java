package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.ProfileViewDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.service.ProfileService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;

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
    public String viewOwnProfile(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if user is logged in
        if (loggedInUser == null) {
            return "redirect:/authority/signin";
        }

        // Profile is only available for STAFF and CUSTOMER, not ADMIN
        if (loggedInUser.getRole() != Role.STAFF && loggedInUser.getRole() != Role.CUSTOMER) {
            return "redirect:/";
        }

        try {
            ProfileViewDto profile = profileService.getProfileView(loggedInUser);
            model.addAttribute("profile", profile);
            model.addAttribute("isOwnProfile", true);
            return "profile/ViewProfile";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải thông tin hồ sơ: " + e.getMessage());
            model.addAttribute("profile", null);
            return "profile/ViewProfile";
        }
    }

    @GetMapping("/admin/{userId}")
    public String viewUserProfile(@PathVariable Integer userId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/authority/signin";
        }

        if (loggedInUser.getRole() != Role.STAFF) {
            return "redirect:/";
        }

        Optional<User> targetUserOpt = authorizedRepo.findById(userId.longValue());
        if (targetUserOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "redirect:/";
        }

        try {
            ProfileViewDto profile = profileService.getProfileView(targetUserOpt.get());
            model.addAttribute("profile", profile);
            model.addAttribute("isAdminView", true);
            model.addAttribute("targetUserId", userId);
            return "profile/ViewProfile";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải thông tin hồ sơ: " + e.getMessage());
            model.addAttribute("profile", null);
            return "profile/ViewProfile";
        }
    }

    @GetMapping("/edit")
    public String editOwnProfile(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if user is logged in
        if (loggedInUser == null) {
            return "redirect:/authority/signin";
        }

        // Profile edit is only available for STAFF and CUSTOMER, not ADMIN
        if (loggedInUser.getRole() != Role.STAFF && loggedInUser.getRole() != Role.CUSTOMER) {
            return "redirect:/";
        }

        try {
            ProfileViewDto profile = profileService.getProfileView(loggedInUser);
            model.addAttribute("profile", profile);
            model.addAttribute("isOwnProfile", true);
            return "profile/EditProfile";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải thông tin hồ sơ: " + e.getMessage());
            return "redirect:/profile/view";
        }
    }

    @GetMapping("/admin/{userId}/edit")
    public String editUserProfile(@PathVariable Integer userId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if user is logged in and is staff (since there's no ADMIN role)
        if (loggedInUser == null) {
            return "redirect:/authority/signin";
        }

        if (loggedInUser.getRole() != Role.STAFF) {
            return "redirect:/";
        }

        Optional<User> targetUserOpt = authorizedRepo.findById(userId.longValue());
        if (targetUserOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "redirect:/";
        }

        try {
            ProfileViewDto profile = profileService.getProfileView(targetUserOpt.get());
            model.addAttribute("profile", profile);
            model.addAttribute("isAdminEdit", true);
            model.addAttribute("targetUserId", userId);
            return "profile/EditProfile";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải thông tin hồ sơ: " + e.getMessage());
            return "redirect:/profile/admin/" + userId;
        }
    }
}

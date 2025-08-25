package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.ProfileViewDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.request.ProfileEditRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.service.ProfileService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuthorizedRepo userRepository;

    @GetMapping("/view")
    public String viewProfile(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/authority/signin";
        }

        try {
            ProfileViewDto profile = profileService.getProfileView(user);
            model.addAttribute("profile", profile);
            model.addAttribute("loggedInUser", user);
            return "profile/ViewProfile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin hồ sơ");
            return redirectToHome(user);
        }
    }

    @GetMapping("/edit")
    public String editProfile(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/authority/signin";
        }

        try {
            ProfileViewDto profile = profileService.getProfileView(user);

            ProfileEditRequest editRequest = ProfileEditRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address1(user.getAddress1())
                .address2(user.getAddress2())
                .firstname(profile.getFirstName())
                .lastname(profile.getLastName())
                .build();

            model.addAttribute("profileEditRequest", editRequest);
            model.addAttribute("loggedInUser", user);
            return "profile/EditProfile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải thông tin hồ sơ");
            return "redirect:/profile/view";
        }
    }

    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute("profileEditRequest") ProfileEditRequest request,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/authority/signin";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("loggedInUser", user);
            return "profile/EditProfile";
        }

        try {
            profileService.updateProfile(user, request);

            User updatedUser = userRepository.findById(user.getUser_id().longValue()).orElse(user);
            session.setAttribute("loggedInUser", updatedUser);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
            return "redirect:/profile/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("loggedInUser", user);
            return "profile/EditProfile";
        }
    }

    private String redirectToHome(User user) {
        return user.getRole() == Role.STAFF ? "redirect:/staff/staff-dashboard" : "redirect:/guest";
    }
}

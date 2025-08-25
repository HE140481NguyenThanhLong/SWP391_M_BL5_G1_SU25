package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.response.UserResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 📝 Trang danh sách account + filter + phân trang
    @GetMapping("/account-list")
    public String getAccounts(@RequestParam(value = "username", required = false, defaultValue = "") String username,
                              @RequestParam(value = "role", required = false) Role role,
                              @RequestParam(value = "status", required = false) UserStatus status,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              Model model) {
        int size = 8;

        Page<UserResponse> userPage = userService.filterUsers(username, role, status, page, size);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        // filter
        model.addAttribute("username", username);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", UserStatus.values());

        return "admin/admin-main-screen";
    }


    // 📝 Trang chỉnh sửa account
    @GetMapping("/account-edit/{id}")
    public String editUserForm(@PathVariable("id") Integer id, Model model) {
        UserResponse user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/account-edit"; // thymeleaf view
    }

    // 📝 Submit cập nhật account
    @PostMapping("/account-edit/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @RequestParam("role") Role role,
                             @RequestParam("status") UserStatus status,
                             RedirectAttributes redirectAttributes) {
        try {
            // cập nhật status
            userService.updateUserStatus(id, status);

            // cập nhật role
            userService.updateUserRole(id, role);

            redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/account-list";
    }

}

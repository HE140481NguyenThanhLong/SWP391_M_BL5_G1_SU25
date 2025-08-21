package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.response.UserResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/account-list")
    public String getAccounts(Model model) {
        List<UserResponse> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin-main-screen";
    }

    // UserController.java
    @GetMapping("/account-edit/{id}")
    public String editUserForm(@PathVariable("id") Integer id, Model model) {
        UserResponse user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/account-edit";
    }

    @PostMapping("/account-edit/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @RequestParam("role") Role role,
                             @RequestParam("status") UserStatus status) {
        userService.updateUser(id, role, status);
        return "redirect:/account-list";
    }


}

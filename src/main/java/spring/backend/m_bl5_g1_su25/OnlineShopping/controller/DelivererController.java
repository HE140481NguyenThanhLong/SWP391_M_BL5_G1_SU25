package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.User;

@Controller
@RequestMapping("/deliverer")
public class DelivererController extends BaseUserController {

    @Override
    protected String getRoleName() {
        return "DELIVERER";
    }

    @Override
    protected String getDashboardTemplate() {
        return "deliverer/deliverer-main-screen";
    }

    @Override
    protected String getProfileRedirectUrl() {
        return "/deliverer/profile";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        return handleDashboard(authentication, model);
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        return handleProfile(authentication, model);
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        return handleUpdateProfile(user, authentication, redirectAttributes);
    }
}

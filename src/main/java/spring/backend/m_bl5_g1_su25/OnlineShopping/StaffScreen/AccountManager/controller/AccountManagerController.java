package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/account-manager")
public class AccountManagerController {

    @GetMapping
    public String accountManager(Model model) {
        model.addAttribute("pageTitle", "Account Manager");
        return "staff/account-manager";
    }
}

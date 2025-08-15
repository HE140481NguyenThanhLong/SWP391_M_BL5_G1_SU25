package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/customer-service")
public class CustomerServiceController {

    @GetMapping
    public String customerService(Model model) {
        model.addAttribute("pageTitle", "Customer Service");
        return "staff/customer-service";
    }
}

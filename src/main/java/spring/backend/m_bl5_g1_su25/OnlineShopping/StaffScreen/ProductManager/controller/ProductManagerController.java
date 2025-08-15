package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/products")
public class ProductManagerController {

    @GetMapping
    public String productManager(Model model) {
        model.addAttribute("pageTitle", "Product Manager");
        return "staff/product-manager";
    }
}

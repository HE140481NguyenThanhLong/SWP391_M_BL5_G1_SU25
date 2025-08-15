package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/orders")
public class OrderListController {

    @GetMapping
    public String orderList(Model model) {
        model.addAttribute("pageTitle", "Order List");
        return "staff/order-list";
    }
}

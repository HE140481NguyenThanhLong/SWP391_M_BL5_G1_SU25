package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.service.StaffOrderService;

import java.util.List;

@Controller
@RequestMapping("/staff/orders")
public class StaffOrderController {

    private final StaffOrderService staffOrderService;

    public StaffOrderController(StaffOrderService staffOrderService) {
        this.staffOrderService = staffOrderService;
    }

    // Trang danh sách đơn hàng
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", staffOrderService.getAllOrders());
        return "staff-order/staff-order-list"; // templates/staff-order/staff-order-list.html
    }

    // Trang chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Integer id, Model model) {
        Order order = staffOrderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "staff-order/staff-order-detail";
    }


    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable("id") Integer id,
                                    @RequestParam("status") OrderStatus status) {
        staffOrderService.updateOrderStatus(id, status);
        return "redirect:/staff/orders";
    }
}


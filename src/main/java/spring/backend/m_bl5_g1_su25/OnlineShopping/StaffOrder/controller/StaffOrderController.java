package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.dto.StaffOrderDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.StaffOrder.service.StaffOrderService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/staff/orders")
public class StaffOrderController {

    private final StaffOrderService staffOrderService;

    public StaffOrderController(StaffOrderService staffOrderService) {
        this.staffOrderService = staffOrderService;
    }

    // Trang danh sách đơn hàng
    @GetMapping
    public String listOrders(@RequestParam(name = "status", required = false) String statusFilter, Model model) {
        List<Order> orders = staffOrderService.getAllOrders();

        // Lọc theo status nếu có
        if (statusFilter != null && !statusFilter.isEmpty()) {
            orders = orders.stream()
                    .filter(o -> o.getStatus().name().equalsIgnoreCase(statusFilter))
                    .collect(Collectors.toList());
        }

        List<StaffOrderDTO> orderDTOs = orders.stream()
                .map(StaffOrderDTO::new)
                .collect(Collectors.toList());

        model.addAttribute("orders", orderDTOs);
        model.addAttribute("statusFilter", statusFilter); // truyền lại để giữ trạng thái selected
        return "staff-order/staff-order-list";
    }

    // Trang chi tiết đơn hàng (dùng DTO)
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Integer id, Model model) {
        Order orderEntity = staffOrderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        StaffOrderDTO order = new StaffOrderDTO(orderEntity); // convert sang DTO
        model.addAttribute("order", order);
        return "staff-order/staff-order-detail";
    }

    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable("id") Integer id,
                                    @RequestParam("status") OrderStatus status) {
        staffOrderService.updateOrderStatus(id, status);
        return "redirect:/staff/orders/detail/" + id;
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service.OrderService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Transaction;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.repository.TransactionRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderService orderService;
    private final TransactionRepository transactionRepository;


    @GetMapping("/order-list")
    public String home(@RequestParam(required = false) String status,
                       @RequestParam(required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                       @RequestParam(required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                       @RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "5") int pageSize,
                       @RequestParam(defaultValue = "createdAt") String orderBy,
                       @RequestParam(defaultValue = "true") boolean isDesc,
                       Model model,
                       HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/authority/signin";
        int userId = loggedInUser.getUser_id();

        // Sắp xếp
        Sort sort = isDesc ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Lấy giỏ hàng theo user có phân trang
        Page<Order> orders = orderService.filterOrders(userId, status == null || status.equals("all") ? null : OrderStatus.valueOf(status), startDate, endDate, pageable);

        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("isDesc", isDesc);


        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("allStatus", OrderStatus.values());
        return "order/order-list";
    }

    @GetMapping("/order-list/{orderId}")
    public String summary(@PathVariable("orderId") Integer orderId, Model model) {
        Order o = orderService.findById(orderId);
        List<OrderDetail> od = orderService.findOrderDetailByOrderId(orderId);
        Transaction trans = transactionRepository.findTransByOrderId(orderId).orElse(null);

        model.addAttribute("order", o);
        model.addAttribute("orderDetails", od);
        model.addAttribute("trans", trans);


        // Tính toán giá trị giỏ hàng
        BigDecimal total = BigDecimal.valueOf(o.getTotal());
        BigDecimal savings = BigDecimal.ZERO; // chỗ này có thể sau này tính mã giảm giá
        BigDecimal storePickup = BigDecimal.ZERO; // phí lấy tại cửa hàng (nếu có)
        BigDecimal originalPrice = total.divide(new BigDecimal("1.08"), 2, RoundingMode.HALF_UP); // thuế 8%

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", total.subtract(originalPrice));
        model.addAttribute("total", total);
        return "order/order-summary";
    }

    // Thay đổi trạng thái đơn hàng
    @PostMapping("/{orderId}/status")
    public String changeStatus(@PathVariable Integer orderId,
                               @RequestParam("status") OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.changeOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/order/order-list?result=fail";
        }
        // quay lại danh sách đơn hàng
        return "redirect:/order/order-list?result=success";
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderService orderService;

    @GetMapping("/order-list")
    public String home(@RequestParam(required = false) OrderStatus status,
                    @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                    @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                    @RequestParam(defaultValue = "0") int pageNo,
                    @RequestParam(defaultValue = "5") int pageSize,
                    @RequestParam(defaultValue = "createdAt") String orderBy,
                    @RequestParam(defaultValue = "true") boolean isDesc,
                   Model model) {

        // --- Giả sử userId lấy từ session (ở đây tạm fix = 4) ---
        int userId = 2;

        // Sắp xếp
        Sort sort = isDesc ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Lấy giỏ hàng theo user có phân trang
            Page<Order> orders = orderService.filterOrders(status, startDate, endDate, pageable);

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

        model.addAttribute("order", o);
        model.addAttribute("orderDetails", od);

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
}

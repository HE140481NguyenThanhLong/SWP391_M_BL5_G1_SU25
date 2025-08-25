package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository.CartItemRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.service.CartItemService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.OrderRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.ShippingInformation;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service.OrderService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.PaymentType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Province;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final CartItemService cartItemService;


    @Transactional
    @PostMapping("create")
    public String createOrder(@ModelAttribute OrderRequest request,
                              HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/authority/signin";
        int userId = loggedInUser.getUser_id();

        request.setUserId(userId);

        Integer result = orderService.checkout(request);

        if(result == null){
            return "redirect:/checkout?result=fail";
        }
        if(request.getPaymentType() != PaymentType.CASH_ON_DELIVERY)
            return "redirect:/payment?orderId=" + result;

        return "redirect:/order/order-list/" + result + "?result=success";
    }



    @GetMapping("")
    public String home(Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/authority/signin";
        int userId = loggedInUser.getUser_id();

        // Tính toán giá trị giỏ hàng
        BigDecimal originalPrice = cartItemService.getCartTotalPrice(userId);
        BigDecimal savings = BigDecimal.ZERO; // chỗ này có thể sau này tính mã giảm giá
        BigDecimal storePickup = BigDecimal.ZERO; // phí lấy tại cửa hàng (nếu có)
        BigDecimal tax = originalPrice.multiply(new BigDecimal("0.08")); // thuế 8%

        BigDecimal total = originalPrice.subtract(savings)
                .add(storePickup)
                .add(tax);

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);

        return "order/checkout";
    }



}

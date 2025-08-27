package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.OrderRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.PaymentRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service.OrderService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Transaction;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.repository.TransactionRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service.TransactionService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import javax.management.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;


    @GetMapping("")
    public String home(Model model,
                       @RequestParam("orderId") int orderId,
                       @RequestParam(value = "cardNumber", required = false) String cardNumber,
                       HttpSession session)
    {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/authority/signin";
        int userId = loggedInUser.getUser_id();


        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng cần thực hiện"));
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán cần thực hiện"));
        List<String> cardNumbers = transactionRepository.findCardNumbers(userId);

        Transaction trans = transactionRepository.findTopByUserIdAndDate(userId).orElse(null);

        if(cardNumbers != null || !cardNumbers.isEmpty()) {
            trans = transactionRepository.findTopByUserIdAndCardNumber(userId, cardNumber).orElse(null);
        }


        model.addAttribute("trans", trans);
        model.addAttribute("payment", payment);
        model.addAttribute("order", order);
        model.addAttribute("cardNumbers", cardNumbers);


// Tính toán giá trị của giỏ hàng
        BigDecimal total = BigDecimal.valueOf(order.getTotal());
        BigDecimal savings = BigDecimal.ZERO; // chỗ này có thể sau này tính mã giảm giá
        BigDecimal storePickup = BigDecimal.ZERO; // phí lấy tại cửa hàng (nếu có)
        BigDecimal originalPrice = total.divide(new BigDecimal("1.08"), 2, RoundingMode.HALF_UP); // thuế

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", total.subtract(originalPrice));
        model.addAttribute("total", total);
        return "payment/payment";
    }
}

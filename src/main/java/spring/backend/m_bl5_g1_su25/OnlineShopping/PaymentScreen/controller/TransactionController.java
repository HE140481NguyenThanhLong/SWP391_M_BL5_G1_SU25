package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.OrderRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.PaymentRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.dto.TransactionDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Transaction;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.TransactionStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service.TransactionService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/create")
    public String create(@ModelAttribute TransactionDTO transaction, Model model) {
        Transaction trans = transactionService.create(transaction);

        model.addAttribute("transaction", trans);
        return "redirect:/order/order-list/" + transaction.getOrderId() + "?result=success&orderId=";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Integer id, Model model) {
        Transaction transaction = transactionService.getById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        model.addAttribute("transaction", transaction);
        return "transaction-detail"; // tên file view (VD: transaction-detail.html, .jsp)
    }

    @GetMapping
    public String getByFilter(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "true") boolean isDesc,
            Model model
    ) {
        Page<Transaction> transactions = transactionService.getByFilter(
                startDate, endDate, pageNo, pageSize, orderBy, isDesc
        );

        model.addAttribute("transactions", transactions.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("isDesc", isDesc);

        return "transaction-list";
    }
}

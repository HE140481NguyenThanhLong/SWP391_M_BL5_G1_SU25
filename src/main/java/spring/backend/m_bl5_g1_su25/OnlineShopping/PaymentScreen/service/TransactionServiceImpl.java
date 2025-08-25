package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.OrderRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.PaymentRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.dto.TransactionDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Transaction;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.TransactionStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.repository.TransactionRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service.TransactionService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepos;
    private final PaymentRepository paymentRepos;

    @Override
    public Transaction create(TransactionDTO transaction) {
        Order order = orderRepos.findById(transaction.getOrderId()).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin đơn hàng"));
        Payment payment = paymentRepos.findById(transaction.getPaymentMethodId()).orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thanh toán"));

        Transaction transEntity = new Transaction();
        BeanUtils.copyProperties(transaction, transEntity);

        transEntity.setOrder(order);
        transEntity.setPaymentMethod(payment);
        transEntity.setStatus(TransactionStatus.SUCCESS);
        transEntity.setVerificationCode(UUID.randomUUID().toString());
        return transactionRepository.save(transEntity);
    }

    @Override
    public Optional<Transaction> getById(Integer id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Page<Transaction> getByFilter(LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         int pageNo,
                                         int pageSize,
                                         String orderBy,
                                         boolean isDesc) {

            Sort sort = isDesc ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

            Page<Transaction> orders = transactionRepository.findByFilters(2, startDate, endDate, pageable);

        return orders;
    }
}


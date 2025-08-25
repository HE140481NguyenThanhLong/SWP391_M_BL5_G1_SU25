package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.dto.TransactionDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionService {
    Transaction create(TransactionDTO transaction);

    Optional<Transaction> getById(Integer id);

    Page<Transaction> getByFilter(LocalDateTime startDate,
                                  LocalDateTime endDate,
                                  int pageNo,
                                  int pageSize,
                                  String orderBy,
                                  boolean isDesc);
}


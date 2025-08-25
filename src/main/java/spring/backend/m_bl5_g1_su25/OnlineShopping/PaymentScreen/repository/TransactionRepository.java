package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("""
        SELECT t FROM Transaction t
        JOIN Payment p ON p.payment_id = t.paymentMethod.payment_id
        WHERE (:userId IS NULL OR p.user.user_id = :userId)
          AND (:startDate IS NULL OR t.createdAt >= :startDate)
          AND (:endDate IS NULL OR t.createdAt <= :endDate)
    """)
    Page<Transaction> findByFilters(Integer userId,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    Pageable pageable);

    @Query(value = """
        SELECT TOP 1 t.*
        FROM [Transaction] t
        JOIN [Payment] p ON p.payment_id = t.payment_id
        WHERE (:userId IS NULL OR p.user_id = :userId)
        ORDER BY t.created_at DESC
    """, nativeQuery = true)
    Optional<Transaction> findTopByUserIdAndDate(@Param("userId") Integer userId);

    @Query(value = """
        SELECT TOP 1 t.*
        FROM [Transaction] t
        JOIN [Payment] p ON p.payment_id = t.payment_id
        WHERE (:userId IS NULL OR p.user_id = :userId)
        AND t.card_number = :cardNumber
        ORDER BY t.created_at DESC
    """, nativeQuery = true)
    Optional<Transaction> findTopByUserIdAndCardNumber(@Param("userId") Integer userId, @Param("cardNumber") String cardNumber);



    @Query("""
        SELECT t.cardNumber FROM Transaction t
        JOIN t.paymentMethod p
        WHERE (:userId IS NULL OR p.user.user_id = :userId)
        ORDER BY t.createdAt DESC
    """)
    List<String> findCardNumbers(Integer userId);


    @Query(value = """
        SELECT TOP 1 t.*
        FROM [Transaction] t
        JOIN [Payment] p ON p.payment_id = t.payment_id
        WHERE (t.order_id = :orderId) AND t.status = 'SUCCESS'
        ORDER BY t.created_at DESC
    """, nativeQuery = true)
    Optional<Transaction> findTransByOrderId(@Param("orderId") Integer orderId);
}

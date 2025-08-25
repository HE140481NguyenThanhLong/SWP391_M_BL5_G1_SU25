package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.OrderRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    public Page<Order> filterOrders(OrderStatus status,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    Pageable pageable);

    public Order changeOrderStatus(Integer orderId, OrderStatus newStatus);

    public Order findById(Integer orderId);

    public List<OrderDetail> findOrderDetailByOrderId(Integer orderId);

    public Integer checkout(OrderRequest request);
    }

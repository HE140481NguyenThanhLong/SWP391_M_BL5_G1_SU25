package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository.CartItemRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.service.CartItemService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.OrderRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.Order;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.OrderDetail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity.ShippingInformation;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.enums.OrderStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.entity.Payment;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.PaymentType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Province;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final CartItemService cartItemService;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepos userRepository;
    private final ProvinceRepository provinceRepository;
    private final WardRepository wardRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    public Page<Order> filterOrders(OrderStatus status,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    Pageable pageable) {
        return orderRepository.findOrdersByFilters(status, startDate, endDate, pageable);
    }

    @Override
    public Order changeOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order findById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<OrderDetail> findOrderDetailByOrderId(Integer orderId) {
        return orderDetailRepository.findOrderDetailByOrder_id(orderId);
    }

    @Override
    public Integer checkout(OrderRequest request) {
        try {
            // 1. Lấy thông tin cần thiết
            User user = userRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Province province = provinceRepository.findById(request.getProvince())
                    .orElseThrow(() -> new RuntimeException("Province not found"));

            Ward ward = wardRepository.findById(request.getWard())
                    .orElseThrow(() -> new RuntimeException("Ward not found"));

            List<Cart_Items> cartItems = cartItemService.getCartByUser(2);
            if (cartItems.isEmpty()) {
                throw new RuntimeException("Cart is empty!");
            }

            // 2. Tạo Payment
            Payment payment = new Payment();
            payment.setPaymentType(PaymentType.CASH_ON_DELIVERY);
            payment.setUser(user);
            paymentRepository.save(payment);

            // 3. Tạo Shipping Information
            ShippingInformation shippingInformation = new ShippingInformation();
            shippingInformation.setRecipientName(request.getName());
            shippingInformation.setPhoneNumber(request.getPhone());
            shippingInformation.setProvinceName(province.getName());
            shippingInformation.setWardName(ward.getName());
            shippingInformation.setDistrictName("District");
            shippingInformation.setStreetAddress(request.getAddress());

            // 4. Tạo Order
            Order order = new Order();
            order.setShippingInformation(shippingInformation);
            order.setStatus(OrderStatus.PENDING);
            order.setPaymentMethod(payment);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            order.setShippingAddress(shippingInformation.getStreetAddress());
            order.setTotal(0.0);
            orderRepository.save(order);

            double total = 0.0;

            // 5. Xử lý Cart Items → OrderDetails
            Set<OrderDetail> orderDetails = new HashSet<>();
            for (Cart_Items cartItem : cartItems) {
                Product product = productRepository.findById(cartItem.getProduct().getProduct_id())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                // Kiểm tra tồn kho
                if (product.getQuantity() < cartItem.getQuantity()) {
                    throw new RuntimeException("Not enough stock for product: " + product.getName());
                }

                // Trừ tồn kho
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                productRepository.save(product);

                // Tính giá cho sản phẩm
                double itemTotal = cartItem.getQuantity() * product.getPrice().doubleValue();
                total += itemTotal;

                // Tạo OrderDetail
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setTotalPrice(product.getPrice());
                orderDetails.add(orderDetail);
            }

            order.setOrderDetails(orderDetails);
            order.setTotal(total);
//            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetails);
            // 6. Xóa giỏ hàng
            cartItemRepository.clearCartByUser(user.getUser_id());

            return order.getOrder_id();

        } catch (Exception e) {
            return null;
        }
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemService {

    public Page<Cart_Items> getCartByUserPaging(Integer userId, Pageable pageable);

    public List<Cart_Items> getCartByUser(Integer userId);

    public BigDecimal getCartTotalPrice(Integer userId);

    @Transactional
    public Cart_Items addToCart(Integer userId, Integer productId, Integer quantity);

    @Transactional
    public Cart_Items updateCartItem(Integer cartItemId, Integer quantity);

    public void deleteCartItem(Integer cartItemId);
}


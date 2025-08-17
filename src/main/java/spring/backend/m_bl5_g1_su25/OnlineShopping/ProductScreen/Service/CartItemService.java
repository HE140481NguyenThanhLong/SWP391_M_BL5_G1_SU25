package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CartItemRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;

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


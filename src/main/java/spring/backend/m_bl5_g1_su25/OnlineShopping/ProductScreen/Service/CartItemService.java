package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Repository.CartItemRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Page<Cart_Items> getCartByUserPaging(Integer userId, Pageable pageable) {
        return cartItemRepository.findByUser_UserId(userId, pageable);
    }

    public List<Cart_Items> getCartByUser(Integer userId) {
        return cartItemRepository.findByUser_UserId(userId);
    }

    public BigDecimal getCartTotalPrice(Integer userId) {
        return cartItemRepository.findByUser_UserId(userId)
                .stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Cart_Items addToCart(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already exists in cart
        Cart_Items existingItem = cartItemRepository.findByUser_UserIdAndProduct_ProductId(userId, productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return cartItemRepository.save(existingItem);
        }

        Cart_Items newItem = Cart_Items.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
        return cartItemRepository.save(newItem);
    }

    @Transactional
    public Cart_Items updateCartItem(Integer cartItemId, Integer quantity) {
        Cart_Items item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void deleteCartItem(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}

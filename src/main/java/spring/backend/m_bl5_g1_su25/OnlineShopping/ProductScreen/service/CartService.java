package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CartItemsRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemsRepository cartItemsRepository;
    private final ProductRepository productRepository;

    // Lấy giỏ hàng theo user
    public List<Cart_Items> getCart(User user) {
        return cartItemsRepository.findByUser(user);
    }

    // Đếm số lượng items trong giỏ
    public Long countCartItems(User user) {
        return cartItemsRepository.countByUser(user);
    }

    // Thêm sản phẩm vào giỏ
    @Transactional
    public void addToCart(User user, Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        Cart_Items cartItem = cartItemsRepository.findByUserAndProduct(user, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = Cart_Items.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }

        cartItemsRepository.save(cartItem);
    }

    // Xóa sản phẩm khỏi giỏ
    @Transactional
    public void removeFromCart(User user, Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        cartItemsRepository.findByUserAndProduct(user, product)
                .ifPresent(cartItemsRepository::delete);
    }

    // Cập nhật số lượng
    @Transactional
    public void updateQuantity(User user, Integer productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        Cart_Items cartItem = cartItemsRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Sản phẩm chưa có trong giỏ"));

        cartItem.setQuantity(quantity);
        cartItemsRepository.save(cartItem);
    }
}

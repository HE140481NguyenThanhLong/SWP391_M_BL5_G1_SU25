package spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository.CartItemRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository.ProductCartRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository.UserRepositoryCart;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;


import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepository cartItemRepository;
    private final ProductCartRepository productCartRepository;
    private final UserRepositoryCart userRepository;

    @Override
    public Page<Cart_Items> getCartByUserPaging(Integer userId, Pageable pageable) {
        return cartItemRepository.findByUser_UserId(userId, pageable);
    }

    @Override
    public List<Cart_Items> getCartByUser(Integer userId) {
        return cartItemRepository.findByUser_UserId(userId);
    }

    @Override
    public BigDecimal getCartTotalPrice(Integer userId) {
        return cartItemRepository.findByUser_UserId(userId)
                .stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //    @Override
//    @Transactional
//    public Cart_Items addToCart(Integer userId, Integer productId, Integer quantity) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Check if product already exists in cart
//        Cart_Items existingItem = cartItemRepository.findByUser_UserIdAndProduct_ProductId(userId, productId);
//        if (existingItem != null) {
//            existingItem.setQuantity(existingItem.getQuantity() + quantity);
//            return cartItemRepository.save(existingItem);
//        }
//
//
//
//        Cart_Items newItem = Cart_Items.builder()
//                .user(user)
//                .product(product)
//                .quantity(quantity)
//                .build();
//        return cartItemRepository.save(newItem);
//    }
    @Override
    @Transactional
    public Cart_Items addToCart(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productCartRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already exists in cart
        Cart_Items existingItem = cartItemRepository.findByUser_UserIdAndProduct_ProductId(userId, productId);

        int totalQuantity = quantity;
        if (existingItem != null) {
            totalQuantity += existingItem.getQuantity();
        }

        if (product.getQuantity() < totalQuantity) {
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }

        if (existingItem != null) {
            existingItem.setQuantity(totalQuantity);
            return cartItemRepository.save(existingItem);
        }

        Cart_Items newItem = Cart_Items.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
        return cartItemRepository.save(newItem);
    }

    @Override
    @Transactional
    public Cart_Items updateCartItem(Integer cartItemId, Integer quantity) {
        Cart_Items item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public void deleteCartItem(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}


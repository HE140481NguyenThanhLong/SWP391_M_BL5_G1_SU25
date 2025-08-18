package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.CartService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;




    @PostMapping("/add/{productId}")
    public ResponseEntity<Map<String, Object>> addToCart(@PathVariable Integer productId,
                                                         @RequestParam(defaultValue = "1") int quantity,
                                                         Principal principal) {
        User user = getUserFromPrincipal(principal);

        cartService.addToCart(user, productId, quantity);
        Long totalItems = cartService.countCartItems(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Đã thêm sản phẩm vào giỏ hàng");
        response.put("totalItems", totalItems);

        return ResponseEntity.ok(response);
    }

    // Helper: Lấy user từ login
    private User getUserFromPrincipal(Principal principal) {
        // Lấy username từ session đăng nhập
        String username = principal.getName();

        // Tìm User trong DB

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}

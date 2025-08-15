package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.Service.CartItemService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final ProductRepository productRepository;

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable Integer productId, Model model) {
        Product pro = productRepository.findById(productId).orElse(null);

        model.addAttribute("productDetail", pro);

        return ("cart/product-detail");
    }

    @GetMapping("")
    public String home(@RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "5") int pageSize,
                       @RequestParam(defaultValue = "createdAt") String orderBy,
                       @RequestParam(defaultValue = "true") boolean isDesc,
                       Model model)
    {

        Sort sort = isDesc ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Cart_Items> cartItems = cartItemService.getCartByUserPaging(4, pageable);


        BigDecimal originalPrice = cartItemService.getCartTotalPrice(4);
        BigDecimal savings = new BigDecimal("0");
        BigDecimal storePickup = new BigDecimal("0");
        BigDecimal tax = originalPrice.multiply(new BigDecimal("0.08"));

        BigDecimal total = originalPrice.subtract(savings)
                .add(storePickup)
                .add(tax);

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);


        model.addAttribute("cartItems", cartItems);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", cartItems.getTotalPages());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("isDesc", isDesc);
        return ("cart/cart");
    }

    // View cart
    @GetMapping("/{userId}")
    public String viewCart(@PathVariable Integer userId, Model model) {
        List<Cart_Items> cartItems = cartItemService.getCartByUser(userId);


        BigDecimal originalPrice = cartItemService.getCartTotalPrice(userId);
        BigDecimal savings = new BigDecimal("0");
        BigDecimal storePickup = new BigDecimal("0");
        BigDecimal tax = originalPrice.multiply(new BigDecimal("0.08"));

        BigDecimal total = originalPrice.subtract(savings)
                .add(storePickup)
                .add(tax);

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);


        model.addAttribute("cartItems", cartItems);
        return "cart/cart"; // templates/cart/cart.html
    }

    // Add to cart
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer userId,
                            @RequestParam Integer productId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        cartItemService.addToCart(userId, productId, quantity);
        return "redirect:/cart";
    }

    // Update cart item
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Integer cartItemId,
                                 @RequestParam Integer quantity,
                                 @RequestParam Integer userId) {
        cartItemService.updateCartItem(cartItemId, quantity);
        return "redirect:/cart";
    }

    // Delete cart item
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam Integer cartItemId,
                                 @RequestParam Integer userId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cart";
    }

}
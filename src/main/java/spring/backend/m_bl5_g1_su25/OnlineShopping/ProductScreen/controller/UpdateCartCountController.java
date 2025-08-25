package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.UpdateCartCountService;

import java.util.List;

@RestController
@RequestMapping("/giohang")
public class UpdateCartCountController {
    private final UpdateCartCountService updateCartCountService;

    public UpdateCartCountController(UpdateCartCountService updateCartCountService) {
        this.updateCartCountService = updateCartCountService;
    }


    @PostMapping("/add")
    public String addToCart(@RequestBody Cart_Items item, HttpSession session) {
        updateCartCountService.addToCart(session, item);
        return "Product added to cart";
    }

    @GetMapping("/count")
    public int getCartCount(HttpSession session) {
        Cart_Items cart = (Cart_Items) session.getAttribute("CART_SESSION");
        return (cart != null) ? cart.getQuantity() : 0;
    }

    @GetMapping("/items")
    public List<Cart_Items> getCartItems(HttpSession session) {
        return updateCartCountService.getCart(session);
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        updateCartCountService.clearCart(session);
        return "Cart cleared";
    }
}

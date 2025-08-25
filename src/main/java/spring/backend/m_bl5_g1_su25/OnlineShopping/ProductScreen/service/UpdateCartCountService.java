package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;

import java.util.*;

@Service
public class UpdateCartCountService {
    private static final String CART_SESSION_KEY = "CART";

    @SuppressWarnings("unchecked")
    public List<Cart_Items> getCart(HttpSession session) {
        return (List<Cart_Items>) session.getAttribute(CART_SESSION_KEY);
    }

    public void addToCart(HttpSession session, Cart_Items item) {
        List<Cart_Items> cart = getCart(session);
        if (cart == null) {
            cart = new ArrayList<>();
        }

        Optional<Cart_Items> existing = cart.stream()
                .filter(c -> c.getProduct() == item.getProduct())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            cart.add(item);
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public int getCartCount(HttpSession session) {
        List<Cart_Items> cart = getCart(session);
        if (cart == null) return 0;
        return cart.stream().mapToInt(Cart_Items::getQuantity).sum();
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}

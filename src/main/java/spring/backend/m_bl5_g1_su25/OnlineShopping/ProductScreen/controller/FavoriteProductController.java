package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.FavoriteService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteProductController {

    @Autowired
    FavoriteService favoriteService;

    @GetMapping("/customer/favorites")
    public String favoriteProducts(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Product> favoriteProducts = favoriteService.getFavoriteProducts(loggedInUser.getUsername());
        model.addAttribute("favoriteProducts", favoriteProducts);
        if (favoriteProducts.isEmpty()) {
            model.addAttribute("message", "You don't have any favorite products");
        }
        return "favoriteScreen/favorite";
    }
}

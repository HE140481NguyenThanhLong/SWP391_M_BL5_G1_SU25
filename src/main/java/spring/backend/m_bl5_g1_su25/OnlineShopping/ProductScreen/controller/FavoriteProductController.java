package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.FavoriteRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.FavoriteService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.List;

@Controller
@RequestMapping("/favoriteScreen")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteProductController {
    @Autowired
    FavoriteService favoriteService;

    @GetMapping("/favorite")
    public String favoriteProducts(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Product> favoriteProducts = favoriteService.getFavoriteProducts(loggedInUser.getUsername());
        model.addAttribute("favoriteProducts", favoriteProducts);
        if (favoriteProducts.isEmpty()) {
            model.addAttribute("message", "You don't have any favorite products");
        }
        return "/favoriteScreen/favorite";

    }
    @PostMapping("/add/{productId}")
    public String addFavorite(@PathVariable Integer productId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        try {
            favoriteService.addFavoriteProduct(loggedInUser.getUsername(), productId);
            redirectAttributes.addFlashAttribute("successMessage", "Product added to your favorites!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/product/detailproduct/" + productId;
    }
    @PostMapping("/delete/{productId}")
    public String deleteFavorite(@PathVariable Integer productId, HttpSession session, RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        try {
            favoriteService.deleteFavoriteProduct(loggedInUser.getUsername(), productId);
            redirectAttributes.addFlashAttribute("successMessage", "Product removed from your favorites!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }


        return "redirect:/favoriteScreen/favorite";
    }

}

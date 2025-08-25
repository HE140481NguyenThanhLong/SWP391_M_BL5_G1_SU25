package spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.service.HomeService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.FavoriteService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    HomeService homeService;
    FavoriteService favoriteService;
    CustomerRepository customerRepository;

    @GetMapping
    public String getAll(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                           HttpSession session ) {

        // Add user info to model for header display


        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = homeService.findAllProduct(pageable);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", products.getContent());

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<Product> favoriteProducts = favoriteService.getFavoriteProducts(loggedInUser.getUsername());

            model.addAttribute("homeFavorite", favoriteProducts);
        }
        return "homeScreen/Home";
    }

    @GetMapping("/getRecent")
    public String getRecent(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size
                           ) {



        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> recentProducts = homeService.getRecentProducts(pageable);
        model.addAttribute("products", recentProducts);
        return "homeScreen/Home";
    }

    @GetMapping("/getLastest")
    public String getLastest(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size
                            ) {



        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> lastestProducts = homeService.getLatestProducts(pageable);
        model.addAttribute("products", lastestProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lastestProducts.getTotalPages());
        model.addAttribute("totalItems", lastestProducts.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", lastestProducts.getContent());



        return "homeScreen/Home";
    }

    @GetMapping("/byCategories/{categories}")
    public String getAllByCategories(Model model, @PathVariable String categories,
                                     @RequestParam(defaultValue = "0")int page,
                                     @RequestParam(defaultValue = "10")int size){
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> byCategory = homeService.getProductsByCategory(pageable,categories);
            model.addAttribute("products", byCategory);

            return "homeScreen/Home";
    }
    @GetMapping ("/byPriceDesc")
    public String getProductsByPriceDesc(Model model,
                                         @RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10")int size){
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> productsByPriceDesc = homeService.getProductsByPriceDesc(pageable);
            model.addAttribute("products", productsByPriceDesc);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsByPriceDesc.getTotalPages());
        model.addAttribute("totalItems", productsByPriceDesc.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", productsByPriceDesc.getContent());
            return "homeScreen/Home";
    }
    @GetMapping("/lastestProduct")
    public String getLastestProduct(Model model){
        ProductResponse latest = homeService
                .findFirstByOrderByCreatedDateDesc()
                .orElse(null);
        model.addAttribute("latestProduct", latest);
        return "homeScreen/Home";
    }
    @GetMapping ("/byPriceAsc")
    public String getProductsByPriceAsc(Model model,
                                         @RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productsByPriceDesc = homeService.getProductsByPriceAsc(pageable);
        model.addAttribute("products", productsByPriceDesc);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsByPriceDesc.getTotalPages());
        model.addAttribute("totalItems", productsByPriceDesc.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", productsByPriceDesc.getContent());
        return "homeScreen/Home";
    }
    @GetMapping("/filter")
    public String filterProduct(@RequestParam(name="keyword",required = false) String keyword,
    Model model,
    @RequestParam(defaultValue = "0")int page,
    @RequestParam(defaultValue = "10")int size){
    Pageable pageable = PageRequest.of(page, size);
    Page<ProductResponse> products=Page.empty();
    if (StringUtils.hasText(keyword) ) {
        products= homeService.getProductsByName(pageable,keyword);

    }else{
        products= homeService.findAllProduct(pageable);
    }
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", products.getContent());
        model.addAttribute("keyword", keyword);
    return "homeScreen/Home";
    }


    @GetMapping("/top5")
    public String getTop5(Model model){
        List<ProductResponse> product5 = homeService.getFiveProductsHottest();
        model.addAttribute("product5", product5);
        return "HomeScreen/Top-Seller";

    }
}

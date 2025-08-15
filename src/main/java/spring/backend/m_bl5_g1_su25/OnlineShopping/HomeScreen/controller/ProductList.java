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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.Status;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
@FieldDefaults(level =AccessLevel.PRIVATE,makeFinal = true)
public class ProductList {
        ProductService productService;
        @GetMapping
    public String getAll(Model model,
                         @RequestParam(defaultValue = "0")int page,
                         @RequestParam(defaultValue = "10")int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> products =productService.findAllProduct(pageable);
            model.addAttribute("products", products);
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", products.getTotalPages());
//            model.addAttribute("totalItems", products.getTotalElements());
//            model.addAttribute("pageSize", size);

        return"HomeScreen/Home";
        }
    @GetMapping("/getRecent")
    public String getRecent(Model model,
                            @RequestParam(defaultValue = "0")int page,
                            @RequestParam(defaultValue = "10")int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> recentProducts = productService.getRecentProducts(pageable);
        model.addAttribute("products", recentProducts);
        return "HomeScreen/Home";

    }

}

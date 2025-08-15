package spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.reponse.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductServiceImpl;

import java.util.List;

@Controller
//@RequiredArgsConstructor
//@RequestMapping("/guest")
@FieldDefaults(level =AccessLevel.PRIVATE,makeFinal = true)
public class ProductList {

    private ProductServiceImpl productService;
    public ProductList(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<ProductResponse> productResponses = productService.toProductResponse();
        model.addAttribute("productResponses", productResponses);
        return "HomeScreen/Home";
    }
}

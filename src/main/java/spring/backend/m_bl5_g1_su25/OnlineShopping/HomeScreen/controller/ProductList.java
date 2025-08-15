package spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/guest")
@FieldDefaults(level =AccessLevel.PRIVATE,makeFinal = true)
public class ProductList {
//        ProductService productService;
//        @GetMapping
//    public String listProducts(Model model) {
//        List<ProductResponse> productResponses = productService.toProductResponse();
//        model.addAttribute("products", productResponses);
//        return"HomeScreen/ProductBoard";
//        }
}

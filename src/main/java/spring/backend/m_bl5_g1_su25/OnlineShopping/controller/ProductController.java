package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.service.ProductService;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "Tất cả danh mục") String category,
            Model model) {
        int pageSize = 10;
        Page<Product> productPage = productService.getFilteredProducts(search, category, page, pageSize);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", productService.getDistinctCategories());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        return "product/product_list";
    }
}

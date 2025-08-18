package spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.controller;

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
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductServiceForHomeScreen;

@Controller
@RequiredArgsConstructor
@RequestMapping("/guest")
@FieldDefaults(level =AccessLevel.PRIVATE,makeFinal = true)
public class ProductList {
        ProductServiceForHomeScreen productServiceForHomeScreen;
        @GetMapping
    public String getAll(Model model,
                         @RequestParam(defaultValue = "0")int page,
                         @RequestParam(defaultValue = "10")int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> products = productServiceForHomeScreen.findAllProduct(pageable);
            model.addAttribute("products", products);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", products.getTotalPages());
            model.addAttribute("totalItems", products.getTotalElements());
            model.addAttribute("pageSize", size);
            model.addAttribute("content", products.getContent());

        return"HomeScreen/Home";
        }
    @GetMapping("/getRecent")
    public String getRecent(Model model,
                            @RequestParam(defaultValue = "0")int page,
                            @RequestParam(defaultValue = "10")int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> recentProducts = productServiceForHomeScreen.getRecentProducts(pageable);
        model.addAttribute("products", recentProducts);
        return "HomeScreen/Home";

    }
    @GetMapping("/getLastest")
    public  String getLastest(Model model,
                              @RequestParam(defaultValue = "0")int page,
                              @RequestParam(defaultValue = "10")int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> lastestProducts = productServiceForHomeScreen.getLatestProducts(pageable);
            model.addAttribute("products", lastestProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lastestProducts.getTotalPages());
        model.addAttribute("totalItems", lastestProducts.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", lastestProducts.getContent());
            return "HomeScreen/Home";
    }
//    @GetMapping("/getLastestwithCategories/{categories}")
//    public String getLastestwithCategories(Model model, @PathVariable String categories,
//                                           @RequestParam(defaultValue = "0")int page,
//                                           @RequestParam(defaultValue = "10")int size){
//             Pageable pageable = PageRequest.of(page, size);
//             Page<ProductResponse> lastestWcategories = productService.getAllByOrderByCreatedAtAscAndCategories(pageable, categories);
//             model.addAttribute("products", lastestWcategories);
//             return "HomeScreen/Home";
//
//    }
//    @GetMapping("/getRecentwithCategories/{categories}")
//    public String getRecentwithCategories(Model model, @PathVariable String categories,
//                                          @RequestParam(defaultValue = "0")int page,
//                                          @RequestParam(defaultValue = "10")int size){
//            Pageable pageable = PageRequest.of(page, size);
//            Page<ProductResponse> recentWcategories = productService.getAllByOrderByCreatedAtDescAndCategories(pageable, categories);
//            model.addAttribute("products", recentWcategories);
//            return "HomeScreen/Home";
//    }
//    @GetMapping("/priceAscwithCategories/{categories}")
//    public String getAllByPriceAscWithCategories(Model model,@PathVariable Double price,@PathVariable String categories,
//                                                 @RequestParam(defaultValue = "0")int page,
//                                                 @RequestParam(defaultValue = "10")int size){
//            Pageable pageable = PageRequest.of(page, size);
//            Page<ProductResponse> priceCategoriesAsc= productService.getAllByCategoryNameOrderByPriceAsc(pageable,categories,price);
//            model.addAttribute("products", priceCategoriesAsc);
//            return "HomeScreen/Home";
//    }
//    @GetMapping("/priceDescWithCategories/{categories}")
//    public String getAllByPriceDescWithCategories(Model model,@PathVariable Double price,@PathVariable String categories,
//                                                 @RequestParam(defaultValue = "0")int page,
//                                                 @RequestParam(defaultValue = "10")int size){
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ProductResponse> priceCategoriesDesc= productService.getAllByCategoryNameOrderByPriceDesc(pageable,categories,price);
//        model.addAttribute("products", priceCategoriesDesc);
//        return "HomeScreen/Home";
//    }
    @GetMapping("/byCategories/{categories}")
    public String getAllByCategories(Model model, @PathVariable String categories,
                                     @RequestParam(defaultValue = "0")int page,
                                     @RequestParam(defaultValue = "10")int size){
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> byCategory = productServiceForHomeScreen.getProductsByCategory(pageable,categories);
            model.addAttribute("products", byCategory);

            return "HomeScreen/Home";
    }
    @GetMapping ("/byPriceDesc")
    public String getProductsByPriceDesc(Model model,
                                         @RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10")int size){
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> productsByPriceDesc = productServiceForHomeScreen.getProductsByPriceDesc(pageable);
            model.addAttribute("products", productsByPriceDesc);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsByPriceDesc.getTotalPages());
        model.addAttribute("totalItems", productsByPriceDesc.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", productsByPriceDesc.getContent());
            return "HomeScreen/Home";
    }
    @GetMapping("/lastestProduct")
    public String getLastestProduct(Model model){
        ProductResponse latest = productServiceForHomeScreen
                .findFirstByOrderByCreatedDateDesc()
                .orElse(null);
        model.addAttribute("latestProduct", latest);
        return "HomeScreen/Home";
    }
    @GetMapping ("/byPriceAsc")
    public String getProductsByPriceAsc(Model model,
                                         @RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productsByPriceDesc = productServiceForHomeScreen.getProductsByPriceAsc(pageable);
        model.addAttribute("products", productsByPriceDesc);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsByPriceDesc.getTotalPages());
        model.addAttribute("totalItems", productsByPriceDesc.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", productsByPriceDesc.getContent());
        return "HomeScreen/Home";
    }
    @GetMapping("/filter")
    public String filterProduct(@RequestParam(name="keyword",required = false) String keyword,
    Model model,
    @RequestParam(defaultValue = "0")int page,
    @RequestParam(defaultValue = "10")int size){
    Pageable pageable = PageRequest.of(page, size);
    Page<ProductResponse> products=Page.empty();
    if (StringUtils.hasText(keyword) ) {
        products= productServiceForHomeScreen.getProductsByName(pageable,keyword);

    }else{
        products= productServiceForHomeScreen.findAllProduct(pageable);
    }
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("content", products.getContent());
        model.addAttribute("keyword", keyword);
    return "HomeScreen/Home";
    }
}

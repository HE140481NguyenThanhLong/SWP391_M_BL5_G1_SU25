package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.Repository.ProductRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.Repository.ProductCartRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service.CartItemService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final ProductCartRepository productCartRepository;
    private final ProductRepo productRepo;

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable Integer productId, Model model) {
        Product pro = productCartRepository.findById(productId).orElse(null);

        if(pro != null) {
            List<Integer> categoryIds = pro.getCategories().stream()
                    .map(Category::getCategory_id) // lấy id
                    .collect(Collectors.toList());
            List<Product> products = productRepo.findProductsWithCategoriesByCategoryId(categoryIds);
            model.addAttribute("products", products);
        }

        model.addAttribute("productDetail", pro);

        return ("cart/product-detail");
    }

    @GetMapping("")
    public String home(@RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "5") int pageSize,
                       @RequestParam(defaultValue = "createdAt") String orderBy,
                       @RequestParam(defaultValue = "true") boolean isDesc,
                       Model model)
    {

        Sort sort = isDesc ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Cart_Items> cartItems = cartItemService.getCartByUserPaging(4, pageable);


        BigDecimal originalPrice = cartItemService.getCartTotalPrice(4);
        BigDecimal savings = new BigDecimal("0");
        BigDecimal storePickup = new BigDecimal("0");
        BigDecimal tax = originalPrice.multiply(new BigDecimal("0.08"));

        BigDecimal total = originalPrice.subtract(savings)
                .add(storePickup)
                .add(tax);


        List<Product> products = productRepo.findBestSeller();

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);

        model.addAttribute("products", products);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", cartItems.getTotalPages());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("isDesc", isDesc);
        return ("cart/cart");
    }

    // Add to cart
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        cartItemService.addToCart(4, productId, quantity);
        return "redirect:/cart";
    }

    // Update cart item
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Integer cartItemId,
                                 @RequestParam Integer quantity,
                                 @RequestParam Integer userId) {
        cartItemService.updateCartItem(cartItemId, quantity);
        return "redirect:/cart";
    }

    // Delete cart item
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam Integer cartItemId,
                                 @RequestParam Integer userId) {
        cartItemService.deleteCartItem(cartItemId);
        return "redirect:/cart";
    }

}
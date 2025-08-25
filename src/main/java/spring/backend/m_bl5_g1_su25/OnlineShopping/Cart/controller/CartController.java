package spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository.ProductCartRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.service.CartItemService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;

import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final ProductCartRepository productCartRepository;

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable Long  productId, Model model) {
        // Lấy sản phẩm chi tiết
        Product product = productCartRepository.findById(productId).orElse(null);

        if (product == null) {
            // Nếu không tìm thấy -> trả về trang lỗi 4 0 4
            return "error/404";
        }

        // Lấy danh sách categoryId của sản phẩm
        List<Integer> categoryIds = product.getCategories().stream()
                .map(Category::getCategory_id)
                .collect(Collectors.toList());

        // Phân trang: lấy 5 sản phẩm liên quan
        Pageable pageable = PageRequest.of(0, 5);

        List<Product> relatedProducts = productCartRepository
                .findByCategoryIds(categoryIds, pageable)
                .getContent()
                .stream()
                .filter(p -> !p.getProduct_id().equals(product.getProduct_id())) // bỏ chính nó
                .collect(Collectors.toList());

        // Đẩy dữ liệu ra view
        model.addAttribute("productDetail", product);
        model.addAttribute("products", relatedProducts);

        return "cart/cart-detail";
    }



    @GetMapping("/cart-list")
    public String home(@RequestParam(defaultValue = "0") int pageNo,
                       @RequestParam(defaultValue = "5") int pageSize,
                       @RequestParam(defaultValue = "createdAt") String orderBy,
                       @RequestParam(defaultValue = "true") boolean isDesc,
                       Model model,
                       HttpSession session) {

        // --- Lấy user từ session ---
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/authority/signin"; // nếu chưa login thì chuyển sang trang login
        }
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/authority/signin";
        int userId = loggedInUser.getUser_id();

        int userId = loggedInUser.getUser_id();

        // --- Sắp xếp & phân trang ---
        Sort sort = isDesc ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // --- Lấy giỏ hàng theo user có phân trang ---
        Page<Cart_Items> cartItems = cartItemService.getCartByUserPaging(userId, pageable);

        // --- Tính toán giá trị giỏ hàng ---
        BigDecimal originalPrice = cartItemService.getCartTotalPrice(userId);
        BigDecimal savings = BigDecimal.ZERO; // sau này có thể thêm mã giảm giá
        BigDecimal storePickup = BigDecimal.ZERO; // phí lấy tại cửa hàng (nếu có)
        BigDecimal tax = originalPrice.multiply(new BigDecimal("0.08")); // thuế 8%

        BigDecimal total = originalPrice.subtract(savings)
                .add(storePickup)
                .add(tax);

        // --- Lấy danh sách sản phẩm bán chạy ---
        List<Product> products = productCartRepository.findBestSeller(PageRequest.of(0, 5));

        // --- Đẩy dữ liệu ra view ---
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", cartItems.getTotalPages());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("isDesc", isDesc);

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("storePickup", storePickup);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);

        model.addAttribute("products", products);

        return "cart/cart-list";
    }



    // Thêm vào giỏ hàng

    // Add to cart
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session) {

        try{
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) return "redirect:/authority/signin";
            int userId = loggedInUser.getUser_id();

            cartItemService.addToCart(userId, productId, quantity);
        }catch (Exception e){
            return "redirect:/cart/cart-list?result=fail";
        }
        return "redirect:/cart/cart-list?result=success";
                            @RequestParam Integer quantity,
                            HttpSession session) {
        // Lấy user từ session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Nếu chưa đăng nhập thì quay lại trang login
            return "redirect:/authority/signin";
        }

        // Lấy userId từ user đang đăng nhập
        Integer userId = loggedInUser.getUser_id();

        // Thêm sản phẩm vào giỏ
        cartItemService.addToCart(userId, productId, quantity);

        return "redirect:/cart/cart-list";
    }

    // Update cart item
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Integer cartItemId,
                                 @RequestParam Integer quantity,
                                 @RequestParam Integer userId) {
        try{
            cartItemService.updateCartItem(cartItemId, quantity);
        }catch (Exception e){
            return "redirect:/cart/cart-list?result=fail";
        }
        return "redirect:/cart/cart-list?result=success";
    }

    // Delete cart item
    @PostMapping("/delete")
    public String deleteCartItem(@RequestParam Integer cartItemId,
                                 @RequestParam Integer userId) {
        try {
            cartItemService.deleteCartItem(cartItemId);
        }catch (Exception e){
            return "redirect:/cart/cart-list?result=fail";
        }
        return "redirect:/cart/cart-list?result=success";
    }
    @GetMapping("/count")
    @ResponseBody
    public int getCartCount(HttpSession session) {
        User loggedIn = (User) session.getAttribute("loggedInUser");
        if (loggedIn == null) return 0;
        return cartItemService.getCartItemCount(loggedIn.getUser_id());
    }


}

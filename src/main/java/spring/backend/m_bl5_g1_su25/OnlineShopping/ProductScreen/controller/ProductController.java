package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        int pageSize = 10; // số sản phẩm mỗi trang

        // Gọi service với các tham số đúng
        Page<Product> productPage = productService.getProducts(search, category, page, pageSize, sortField, sortDir);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("stats", productService.getStats());
        model.addAttribute("categories", productService.getAllCategories());

        return "product/product_list";
    }

    @GetMapping("/detail/{id}")
    public String detailProduct(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/product_detail";
    }
    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Integer id,
            @ModelAttribute Product product,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model) {
        try {
            Product existingProduct = productService.getProductById(id);
            if (existingProduct != null) {
                // Cập nhật các trường từ form
                existingProduct.setName(product.getName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setSalePrice(product.getSalePrice());
                existingProduct.setPrice(product.getPrice());

                // PHẦN XỬ LÝ UPLOAD ẢNH (CỐT LÕI)
                if (image != null && !image.isEmpty()) {
                    // 1. Tạo thư mục uploads trong thư mục tạm của hệ thống
                    String tmpDir = System.getProperty("java.io.tmpdir");
                    String uploadDir = tmpDir + "/ecom-uploads/";

                    // 2. Đảm bảo thư mục tồn tại
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // 3. Tạo tên file duy nhất
                    String fileName = "img_" + System.currentTimeMillis() +
                            image.getOriginalFilename().substring(
                                    image.getOriginalFilename().lastIndexOf("."));

                    // 4. Lưu file
                    Path filePath = uploadPath.resolve(fileName);
                    image.transferTo(filePath);

                    // 5. Lưu đường dẫn tương đối
                    existingProduct.setImageUrl("/temp-uploads/" + fileName);
                }

                productService.saveProduct(existingProduct);
                return "redirect:/product/detail/" + id;
            } else {
                model.addAttribute("error", "Sản phẩm không tồn tại");
                return "error";
            }
        }catch (Exception e) {
            model.addAttribute("error", "Lỗi khi cập nhật: " + e.getMessage());
            return "error";
        }

    }
    @GetMapping("/import")
    public String importProduct(Model model) {
        // Lấy danh sách nhà cung cấp và sản phẩm từ DB
        List<String> suppliers = productService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "product/import_product";
    }}
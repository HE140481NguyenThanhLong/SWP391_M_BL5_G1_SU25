package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Supplier;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CategoryRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.SupplierService;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryRepository categoryRepository;
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

        return "product/products_manage";
    }

    @GetMapping("/detail/{id}")
    public String detailProduct(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/Edit_detail";
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
    @GetMapping("/list")
    public String productList(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String supplier,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(name = "category_id", required = false) Integer categoryId,
            Model model
    ) {
        // Xác định tên category để hiển thị
        String selectedCategory = "Tất cả sản phẩm";
        if (categoryId != null) {
            Category categoryEntity = categoryRepository.findById(categoryId).orElse(null);
            if (categoryEntity != null) {
                selectedCategory = categoryEntity.getName();
            }
        }

        Page<Product> products = productService.filterProducts(
                minPrice, maxPrice, supplier, categoryId, sortBy, page, size
        );

        int totalPages = products.getTotalPages();

        // ✅ Tạo danh sách pageNumbers hiển thị quanh currentPage
        List<Integer> pageNumbers = new ArrayList<>();
        if (totalPages > 0) {
            int start = Math.max(0, page - 2);
            int end = Math.min(totalPages - 1, page + 2);
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }
        }

        // Truyền dữ liệu sang view
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("selectedCategory", selectedCategory);

        return "product/list";
    }


    @GetMapping("/detailproduct/{id}")
    public String detailProductInformation(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model) {

        Optional<Product> productOptional = productService.findById(id);
        Product product = productOptional.orElse(null);

        if (product == null) {
            model.addAttribute("errorMessage", "Product not found with id: " + id);
            return "error/404";
        }

        // Lấy sản phẩm liên quan có phân trang
        Page<Product> relatedProducts = productService.findRelatedProducts(id, page, size);

        int totalPages = relatedProducts.getTotalPages();
        List<Integer> pageNumbers = new ArrayList<>();
        if (totalPages > 0) {
            for (int i = 0; i < totalPages; i++) {
                pageNumbers.add(i);
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts.getContent());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", page);

        return "product/detail";
    }
    @GetMapping("/import")
    public String importProduct(Model model) {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<Product> products = productService.getAllProducts();

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("products", products);

        return "product/import_product";
    }
}
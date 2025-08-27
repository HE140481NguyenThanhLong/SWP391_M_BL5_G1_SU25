package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CategoryRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Product> getProducts(String search, String category, int page, int size,
                                     String sortField, String sortDir) {
        if (search != null && search.trim().isEmpty()) search = null;
        if (category != null && category.trim().isEmpty()) category = null;

        // ép luôn sortField = "product_id" và sortDir = "desc"
        Sort sort = Sort.by("product_id").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return productRepository.searchProducts(search, category, pageable);
    }

    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalProducts", productRepository.count());
        stats.put("inStock", productRepository.countByQuantityGreaterThan(0));
        stats.put("lowStock", productRepository.countByQuantityBetween(1, 5));
        stats.put("outOfStock", productRepository.countByQuantity(0));
        return stats;
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));
    }
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategoryName(String categoryName) {
        return productRepository.findByCategories_Name(categoryName);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }


//    public List<String> getAllSuppliers() {
//        return productRepository.findAllSuppliers();
//    }

    public Page<Product> filterProducts(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer categoryId,
            String brand,
            String keyword,
            String sortBy,
            int page,
            int size
    ) {
        Pageable pageable;

        // ✅ Special rule cho 3 trang đầu
        if (page >= 0 && page <= 2) {
            pageable = PageRequest.of(page, size,
                    Sort.by("quantity").descending()
                            .and(Sort.by("createdAt").ascending()));
        } else {
            switch (sortBy) {
                case "price_asc":
                    pageable = PageRequest.of(page, size, Sort.by("price").ascending());
                    break;
                case "price_desc":
                    pageable = PageRequest.of(page, size, Sort.by("price").descending());
                    break;
                case "sold_count":
                    pageable = PageRequest.of(page, size, Sort.by("soldCount").descending());
                    break;
                default:
                    pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            }
        }

        // ✅ Specification
        Specification<Product> spec = Specification.allOf();

        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Object, Object> categoryJoin = root.join("categories", JoinType.INNER);
                return cb.equal(categoryJoin.get("id"), categoryId);
            });
        }

        if (brand != null && !brand.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("brand"), brand));
        }

        if (keyword != null && !keyword.isEmpty()) {
            String likePattern = "%" + keyword.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), likePattern));
        }

        return productRepository.findAll(spec, pageable);
    }




    //    public List<String> getSuppliers() {
//        return productRepository.findAllSuppliers();
//    }
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

//    public List<Product> findRelatedProducts(Integer id) {
//        // Example: Find products in the same category
//        Optional<Product> productOptional = findById(id);
//        Product product = productOptional.orElse(null);
//        if (product == null) {
//            return Collections.emptyList(); // Trả về danh sách rỗng nếu sản phẩm không tồn tại
//        }
//        return productRepository.findByCategoriesIn(product.getCategories());
//    }
    public Page<Product> findRelatedProducts(Integer productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findRelatedProducts(productId, pageable);
    }
    public void importProducts(List<Product> importedProducts) {
        for (Product p : importedProducts) {
            Product existing = productRepository.findById(p.getProduct_id())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + p.getProduct_id()));
            existing.setQuantity(existing.getQuantity() + p.getQuantity()); // cộng thêm số lượng nhập
            productRepository.save(existing);
        }
    }


    public List<Product> getAllProducts() {

            return productRepository.findAll();
    }
    @Transactional
    public void updateQuantity(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() + quantity); // nhập thêm => cộng dồn
        productRepository.save(product);
    }

}
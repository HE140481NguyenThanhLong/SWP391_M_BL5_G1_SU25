 package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CategoryRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Product> getInventory(String search, String category, String stockStatus, int page, int size,
                                      String sortField, String sortDir) {
        Specification<Product> spec = Specification.allOf();
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(root.get("name"), "%" + search + "%"),
                    cb.like(root.get("sku"), "%" + search + "%")
            ));
        }
        if (category != null && !category.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<Object, Object> categoryJoin = root.join("categories", JoinType.INNER);
                return cb.equal(categoryJoin.get("name"), category);
            });
        }
        if (stockStatus != null && !stockStatus.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                if ("in-stock".equals(stockStatus)) {
                    return cb.greaterThan(root.get("quantity"), root.get("minQuantity"));
                } else if ("low-stock".equals(stockStatus)) {
                    return cb.and(
                            cb.greaterThan(root.get("quantity"), 0),
                            cb.lessThanOrEqualTo(root.get("quantity"), root.get("minQuantity"))
                    );
                } else if ("out-of-stock".equals(stockStatus)) {
                    return cb.equal(root.get("quantity"), 0);
                }
                return null;
            });
        }

        Sort sort = Sort.by(sortField);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return productRepository.findAll(spec, pageable);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productRepository.count());
        stats.put("inStock", productRepository.countByQuantityGreaterThanMinQuantity());
        stats.put("lowStock", productRepository.countByQuantityBetween(1, "minQuantity"));
        stats.put("outOfStock", productRepository.countByQuantity(0));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        stats.put("inventoryValue", currencyFormat.format(getInventoryValue()));
        return stats;
    }

    public BigDecimal getInventoryValue() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(p -> p.getImportPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void importProduct(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + productId));
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}

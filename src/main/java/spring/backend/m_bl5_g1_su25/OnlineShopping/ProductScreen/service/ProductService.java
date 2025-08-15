package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CategoryRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Sort sort = Sort.by(sortField);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();

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

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }


    public List<String> getAllSuppliers() {
        return productRepository.findAllSuppliers();
    }


}
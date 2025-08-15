package spring.backend.m_bl5_g1_su25.OnlineShopping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findAll(pageable);
    }
    public List<String> getDistinctCategories() {
        return productRepository.findDistinctCategories();
    }
    public Page<Product> getFilteredProducts(String name, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return productRepository.findByNameAndCategory(name, category, pageable);
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

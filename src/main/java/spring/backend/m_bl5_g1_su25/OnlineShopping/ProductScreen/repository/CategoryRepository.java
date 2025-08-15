package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
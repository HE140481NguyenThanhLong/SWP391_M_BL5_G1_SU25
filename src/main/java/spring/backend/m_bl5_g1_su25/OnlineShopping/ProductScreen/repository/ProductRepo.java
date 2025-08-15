package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Override
    Page<Product> findAll(Pageable page);
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

}

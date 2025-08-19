package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepositoryForHomeScreen extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable page);
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Product> findAllByOrderByCreatedAtAsc(Pageable pageable);
    Page<Product> findAllByCategories(Pageable pageable, String categories);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
//    Page<Product> findAllByCategoryNameOrderByPriceDesc(Pageable pageable, String categories, Double price);
//    Page<Product> findAllByCategoryNameOrderByPriceAsc(Pageable pageable, String categories, Double price);
//    Page<Product> findAllByOrderByCreatedAtDescAndCategories(Pageable pageable, String categories);
//    Page<Product> findAllByOrderByCreatedAtAscAndCategories(Pageable pageable, String categories);
    Page<Product> findByNameContainingIgnoreCase(Pageable pageable,String keyword);
    List<Product> findTop5ByOrderBySoldCountDesc();


    Optional<Product> findFirstByOrderByCreatedAtDesc();
}

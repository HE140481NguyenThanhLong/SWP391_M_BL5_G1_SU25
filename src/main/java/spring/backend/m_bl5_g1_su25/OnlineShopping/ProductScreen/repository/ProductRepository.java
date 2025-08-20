package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query("""
    SELECT DISTINCT p 
    FROM Product p 
    LEFT JOIN p.categories c
    WHERE (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (
           :category IS NULL OR :category = '' 
           OR LOWER(c.name) = LOWER(:category)
           OR (:category = 'LOW_STOCK' AND p.quantity BETWEEN 1 AND 5)
           OR (:category = 'OUT_OF_STOCK' AND p.quantity = 0)
      )
""")
    Page<Product> searchProducts(String search, String category, Pageable pageable);

    long countByQuantityGreaterThan(int qty);
    long countByQuantityBetween(int min, int max);
    long countByQuantity(int qty);
    // Thêm phương thức tùy chỉnh để lấy danh sách nhà cung cấp duy nhất
//    @Query("SELECT DISTINCT p.supplier FROM Product p WHERE p.supplier IS NOT NULL")
//    List<String> findAllSuppliers();

    List<Product> findByCategoriesIn(Set<Category> categories);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name = :name")
    List<Product> findByCategories_Name(@Param("name") String name);
    @Query("SELECT p FROM Product p JOIN p.categories c " +
            "WHERE c IN (SELECT c2 FROM Product pr JOIN pr.categories c2 WHERE pr.product_id = :productId) " +
            "AND p.product_id <> :productId")
    Page<Product> findRelatedProducts(@Param("productId") Integer productId, Pageable pageable);

}



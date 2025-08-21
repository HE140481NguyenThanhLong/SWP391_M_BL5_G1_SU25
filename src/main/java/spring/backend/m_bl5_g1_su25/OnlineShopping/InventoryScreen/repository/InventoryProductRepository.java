package spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;

@Repository
public interface InventoryProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p ORDER BY p.quantity ASC, p.name ASC")
    Page<Product> findAllInventoryProducts(Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.quantity ASC, p.name ASC")
    List<Product> findAllInventoryProductsList();

    @Query("SELECT p FROM Product p WHERE " +
           "(:status = 'all' OR " +
           "(:status = 'in-stock' AND p.quantity > 10) OR " +
           "(:status = 'low-stock' AND p.quantity > 0 AND p.quantity <= 10) OR " +
           "(:status = 'out-of-stock' AND p.quantity = 0)) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY p.quantity ASC, p.name ASC")
    Page<Product> findByStatusAndKeyword(@Param("status") String status,
                                        @Param("keyword") String keyword,
                                        Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
           "(:status = 'all' OR " +
           "(:status = 'in-stock' AND p.quantity > 10) OR " +
           "(:status = 'low-stock' AND p.quantity > 0 AND p.quantity <= 10) OR " +
           "(:status = 'out-of-stock' AND p.quantity = 0)) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY p.quantity ASC, p.name ASC")
    List<Product> findByStatusAndKeywordList(@Param("status") String status,
                                            @Param("keyword") String keyword);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity > 0 AND p.quantity <= 10")
    Long countLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity = 0")
    Long countOutOfStockProducts();
}

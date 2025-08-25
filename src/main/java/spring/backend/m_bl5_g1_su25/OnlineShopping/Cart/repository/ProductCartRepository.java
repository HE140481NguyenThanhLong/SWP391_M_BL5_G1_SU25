package spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductCartRepository extends JpaRepository<Product, Long> {

    // Tìm theo tên chứa từ khóa
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Lọc theo khoảng giá
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Lọc theo nhiều categoryId
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.category_id IN :categoryIds")
    Page<Product> findByCategoryIds(@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);


    // Lấy top 10 sản phẩm bán chạy
//    List<Product> findTop10ByOrderBySalesCountDesc();

    // Hoặc dạng pageable
    @Query("SELECT p FROM Product p ORDER BY p.soldCount DESC")
    List<Product> findBestSeller(Pageable pageable);



}


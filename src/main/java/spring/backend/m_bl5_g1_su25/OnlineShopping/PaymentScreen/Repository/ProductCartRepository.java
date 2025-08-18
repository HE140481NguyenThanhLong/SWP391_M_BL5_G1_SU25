package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductCartRepository extends JpaRepository<Product, Integer> {
    // Tìm theo tên chứa từ khóa
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Lọc theo trạng thái (IN_STOCK, OUT_OF_STOCK, DISCONTINUED)
//    List<Product> findByStatus(Status status);

    // Lọc theo khoảng giá
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Lấy top sản phẩm bán chạy
//    List<Product> findTop10ByOrderBySalesCountDesc();
}

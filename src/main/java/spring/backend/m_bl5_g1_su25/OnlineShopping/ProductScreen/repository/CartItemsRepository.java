package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<Cart_Items, Integer> {

    // Tìm tất cả sản phẩm trong giỏ theo user
    List<Cart_Items> findByUser(User user);

    // Kiểm tra sản phẩm đã có trong giỏ chưa
    Optional<Cart_Items> findByUserAndProduct(User user, Product product);

    // Đếm tổng số item trong giỏ hàng
    Long countByUser(User user);
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Cart_Items;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<Cart_Items, Integer> {
    @Query("""
        Select c from Cart_Items c
        Where c.user.user_id = :user_id
    """)
    Page<Cart_Items> findByUser_UserId(@Param("user_id") Integer user_id, Pageable pageable);

    @Query("""
        Select c from Cart_Items c
        Where c.user.user_id = :user_id
    """)
    List<Cart_Items> findByUser_UserId(@Param("user_id") Integer user_id);

    @Query("""
        Select c from Cart_Items c
        Where c.user.user_id = :user_id AND c.product.product_id = :product_id
    """)
    Cart_Items findByUser_UserIdAndProduct_ProductId(Integer userId, Long productId);
}

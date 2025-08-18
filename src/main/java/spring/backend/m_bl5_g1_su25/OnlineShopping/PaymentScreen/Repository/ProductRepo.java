package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Override
    List<Product> findAll();

    List<Product> findProductsWithCategoriesByCategoryId(List<Integer> categoryIds);

    List<Product> findBestSeller();
}

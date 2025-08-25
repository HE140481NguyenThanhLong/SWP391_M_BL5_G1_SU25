package spring.backend.m_bl5_g1_su25.OnlineShopping.StaffDashBoardScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.Status;

@Repository
public interface DashboardProductRepository extends JpaRepository<Product, Integer> {
@Query("SELECT COUNT(p) FROM Product p")
long countAllProducts();
@Query("SELECT COUNT(p) FROM Product p WHERE p.quantity <= 10 AND p.quantity > 0")
long countLowStockProducts();
@Query("SELECT COUNT(p) FROM Product p WHERE p.quantity = 0 OR p.status = :outOfStock")
long countOutOfStockProducts(Status outOfStock);
default long countOutOfStockProducts() {
        return countOutOfStockProducts(Status.OUT_STOCK);
    }
}

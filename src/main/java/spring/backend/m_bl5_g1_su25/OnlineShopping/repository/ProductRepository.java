package spring.backend.m_bl5_g1_su25.OnlineShopping.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.Product;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
    Page<Product> findByNameContainingIgnoreCaseAndCategory(String name, String category, Pageable pageable);

    default Page<Product> findByNameAndCategory(String name, String category, Pageable pageable) {
        if (name == null) name = "";
        if (category == null || category.equals("Tất cả danh mục")) category = "";
        return findByNameContainingIgnoreCaseAndCategory(name, category, pageable);
    }


}
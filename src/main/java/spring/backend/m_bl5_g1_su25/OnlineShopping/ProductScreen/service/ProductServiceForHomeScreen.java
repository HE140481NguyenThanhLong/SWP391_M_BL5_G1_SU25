package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;

import java.util.Optional;

@Service
public interface ProductServiceForHomeScreen {
    Optional<ProductResponse> findFirstByOrderByCreatedDateDesc();
    Page<ProductResponse> findAllProduct(Pageable page);
    Page<ProductResponse> getRecentProducts(Pageable page);
    Page<ProductResponse> getProductsByCategory(Pageable page, String categories);
    Page<ProductResponse> getProductsByPriceDesc(Pageable page);
    Page<ProductResponse> getProductsByPriceAsc(Pageable page);
    Page<ProductResponse> getLatestProducts(Pageable page);
    Page<ProductResponse> getProductsByName(Pageable page,String name);
//    Page<ProductResponse> getAllByCategoryNameOrderByPriceDesc(Pageable pageable, String categories, Double price);
//    Page<ProductResponse> getAllByCategoryNameOrderByPriceAsc(Pageable pageable, String categories, Double price);
//    Page<ProductResponse> getAllByOrderByCreatedAtDescAndCategories(Pageable pageable, String categories);
//    Page<ProductResponse> getAllByOrderByCreatedAtAscAndCategories(Pageable pageable, String categories);
}

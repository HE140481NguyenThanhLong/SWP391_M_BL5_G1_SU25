package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.mapper.ProductMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepositoryForHomeScreen;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequiredArgsConstructor


public class ProductServiceForHomeScreenImpl implements ProductServiceForHomeScreen {
    final ProductRepositoryForHomeScreen productRepositoryForHomeScreen;
    final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getProductsByName(Pageable page, String name) {
        return productRepositoryForHomeScreen.findByNameContainingIgnoreCase(page, name).map(productMapper::toProductResponse);
    }

    @Override
    public Optional<ProductResponse> findFirstByOrderByCreatedDateDesc() {
        return productRepositoryForHomeScreen.findFirstByOrderByCreatedAtDesc().map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> findAllProduct(Pageable page) {
        return productRepositoryForHomeScreen.findAll(page).map(productMapper::toProductResponse);
    }
    @Override
    public Page<ProductResponse> getRecentProducts(Pageable page) {
        return productRepositoryForHomeScreen.findAllByOrderByCreatedAtDesc(page)
                .map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Pageable page, String categories) {
        return productRepositoryForHomeScreen.findAllByCategories(page, categories).map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPriceDesc(Pageable page) {
        return productRepositoryForHomeScreen.findAllByOrderByPriceDesc(page).map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPriceAsc(Pageable page) {
        return productRepositoryForHomeScreen.findAllByOrderByPriceAsc(page).map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getLatestProducts(Pageable page) {
        return productRepositoryForHomeScreen.findAllByOrderByCreatedAtAsc(page)
                .map(productMapper::toProductResponse);
    }

//    @Override
//    public Page<ProductResponse> getAllByCategoryNameOrderByPriceDesc(Pageable pageable, String categories, Double price) {
//        return productRepo.findAllByCategoryNameOrderByPriceDesc(pageable,categories,price).
//                map(productMapper::toProductResponse);
//    }
//
//    @Override
//    public Page<ProductResponse> getAllByCategoryNameOrderByPriceAsc(Pageable pageable, String categories, Double price) {
//        return productRepo.findAllByCategoryNameOrderByPriceAsc(pageable,categories,price).
//                map(productMapper::toProductResponse);
//    }
//
//    @Override
//    public Page<ProductResponse> getAllByOrderByCreatedAtDescAndCategories(Pageable pageable, String categories) {
//        return productRepo.findAllByOrderByCreatedAtDescAndCategories(pageable,categories)
//                .map(productMapper::toProductResponse);
//    }
//
//    @Override
//    public Page<ProductResponse> getAllByOrderByCreatedAtAscAndCategories(Pageable pageable, String categories) {
//        return productRepo.findAllByOrderByCreatedAtAscAndCategories(pageable,categories)
//                .map(productMapper::toProductResponse);
//    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.mapper.HomeMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.repository.HomeRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequiredArgsConstructor


public class HomeServiceImpl implements HomeService {
    final HomeRepo homeRepo;
    final HomeMapper homeMapper;

    @Override
    public Page<ProductResponse> getProductsByName(Pageable page, String name) {
        return homeRepo.findByNameContainingIgnoreCase(page, name).map(homeMapper::toProductResponse);
    }

    @Override
    public List<ProductResponse> getFiveProductsHottest() {
        // FIX: Call the method that actually has your @Query annotation.
        List<Product> products = homeRepo.findTop5ByOrderBySoldCountDesc(PageRequest.of(0, 5));

        // Safety Check
        if (products == null) {
            return new ArrayList<>();
        }

        // Map the results to your response DTO
        return products.stream()
                .map(homeMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> findFirstByOrderByCreatedDateDesc() {
        return homeRepo.findFirstByOrderByCreatedAtDesc().map(homeMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> findAllProduct(Pageable page) {
        return homeRepo.findAll(page).map(homeMapper::toProductResponse);
    }
    @Override
    public Page<ProductResponse> getRecentProducts(Pageable page) {
        return homeRepo.findAllByOrderByCreatedAtDesc(page)
                .map(homeMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Pageable page, String categories) {
        return homeRepo.findAllByCategories(page, categories).map(homeMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPriceDesc(Pageable page) {
        return homeRepo.findAllByOrderByPriceDesc(page).map(homeMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByPriceAsc(Pageable page) {
        return homeRepo.findAllByOrderByPriceAsc(page).map(homeMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getLatestProducts(Pageable page) {
        return homeRepo.findAllByOrderByCreatedAtAsc(page)
                .map(homeMapper::toProductResponse);
    }


}

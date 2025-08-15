package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.mapper.ProductMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepo;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequiredArgsConstructor


public class ProductServiceImpl implements ProductService {
    final  ProductRepo productRepo;
    final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> findAllProduct(Pageable page) {
        return productRepo.findAll(page).map(productMapper::toProductResponse);
    }
    @Override
    public Page<ProductResponse> getRecentProducts(Pageable page) {
        return productRepo.findAllByOrderByCreatedAtDesc(page)
                .map(productMapper::toProductResponse);
    }

    //    @Override
//    public List<ProductResponse> toProductResponse() {
//        List<ProductResponse> productResponses = productRepo.findAll()
//                .stream()
//                .map(productMapper::toProductResponse)
//                .toList();
//        if (productResponses.isEmpty()) {
//            throw new RuntimeException("No products found");
//        }
//
//        return productResponses;
//    }

}

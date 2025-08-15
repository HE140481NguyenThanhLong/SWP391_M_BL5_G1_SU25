package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequiredArgsConstructor


public class ProductServiceImpl implements ProductService {
//    ProductRepo productRepo;
//    ProductMapper productMapper;
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

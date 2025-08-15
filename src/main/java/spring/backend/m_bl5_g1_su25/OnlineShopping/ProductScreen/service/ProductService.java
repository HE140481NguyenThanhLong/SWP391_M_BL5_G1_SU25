package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.util.List;

@Service
public interface ProductService {

    Page<ProductResponse> findAllProduct(Pageable page);
    Page<ProductResponse> getRecentProducts(Pageable page);
}

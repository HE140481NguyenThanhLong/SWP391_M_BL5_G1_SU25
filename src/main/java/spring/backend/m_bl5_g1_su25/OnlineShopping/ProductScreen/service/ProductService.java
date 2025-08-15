package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.reponse.ProductResponse;

import java.util.List;

@Service
public interface ProductService {
    List<ProductResponse> toProductResponse();
}

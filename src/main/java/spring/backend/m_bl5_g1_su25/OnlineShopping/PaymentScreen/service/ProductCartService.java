package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.service;

import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.dto.reponse.ProductResponse;

import java.util.List;

@Service
public interface ProductCartService {
    List<ProductResponse> toProductResponse();
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;

public interface AuthorizedService {
    Customer signUp(SignUpRequest request);
}

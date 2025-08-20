package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

public interface AuthorizedService {
    Customer signUp(SignUpRequest request);
    User login(String username, String password);


}

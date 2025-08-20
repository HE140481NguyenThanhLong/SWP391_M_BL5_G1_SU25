package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;

import java.time.LocalDateTime;

public class AuthorizedServiceImpl implements  AuthorizedService {
    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    AuthorizedRepo authorizedRepo;

    @Override
    public Customer signUp(SignUpRequest request) {
        User user=modelMapper.map(request,User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = authorizedRepo.save(user);

        Customer customer=modelMapper.map(savedUser,Customer.class);
        customer.setUser(savedUser); // set user into customer
        user.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public User login(String email, String password) {
        return null;
    }
}

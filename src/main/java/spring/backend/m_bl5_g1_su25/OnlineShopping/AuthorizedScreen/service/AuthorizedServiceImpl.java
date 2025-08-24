package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
        customer.setUser(savedUser);// set user into customer
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
        user.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public User login(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }
    public User findUserByUsername(String username) {
        return authorizedRepo.findByUsername(username).orElse(null);
    }

}

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
    ModelMapper modelMapper;
    AuthorizedRepo authorizedRepo;
    PasswordEncoder passwordEncoder;

    @Override
    public Customer signUp(SignUpRequest request) {
        // Create User entity
        User user = modelMapper.map(request, User.class);
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now()); // Set createdAt before saving

        // Save User first
        User savedUser = authorizedRepo.save(user);

        // Create Customer entity
        Customer customer = modelMapper.map(savedUser, Customer.class);
        customer.setUser(savedUser); // Link customer to user
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());

        // Save and return Customer
        return customerRepository.save(customer);
    }

    @Override
    public User login(String username, String password) {
        User user = findUserByUsername(username);
        // Use PasswordEncoder to check password
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User findUserByUsername(String username) {
        return authorizedRepo.findByUsername(username).orElse(null);
    }
}

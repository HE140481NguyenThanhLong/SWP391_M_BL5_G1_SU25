package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class AuthorizedServiceImpl implements AuthorizedService {
    private final CustomerRepository customerRepository;
    private final AuthorizedRepo authorizedRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = authorizedRepo.save(user);

        Customer customer = Customer.builder()
                .user(savedUser)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();

        return customerRepository.save(customer);
    }
}

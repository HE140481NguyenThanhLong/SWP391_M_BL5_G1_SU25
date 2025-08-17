package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.dto.RegisterRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.StaffRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create username by merging firstname and lastname
        String username = request.getFirstname() + " " + request.getLastname();

        // Create new user với thông tin cơ bản
        User user = User.builder()
                .username(username) // Merged firstname + lastname
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress()) // Add address field
                .role(request.getRole())
                .status(User.Status.ACTIVE)
                .isDeleted(false)
                .build();

        User savedUser = userRepository.save(user);

        // Tạo Customer hoặc Staff entity tương ứng dựa trên role
        createUserProfile(savedUser, request);

        log.info("User registered successfully: {} ({})", savedUser.getUsername(), savedUser.getEmail());
        return savedUser;
    }

    private void createUserProfile(User user, RegisterRequest request) {
        switch (user.getRole()) {
            case CUSTOMER -> {
                Customer customer = Customer.builder()
                        .user(user)
                        .firstname(request.getFirstname())  // Use firstname directly from request
                        .lastname(request.getLastname())    // Use lastname directly from request
                        .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : "")
                        .build();
                customerRepository.save(customer);
                log.info("Customer profile created for user: {}", user.getUsername());
            }
            case STAFF -> {
                Staff staff = Staff.builder()
                        .user(user)
                        .firstname(request.getFirstname())  // Use firstname directly from request
                        .lastname(request.getLastname())    // Use lastname directly from request
                        .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : "")
                        .build();
                staffRepository.save(staff);
                log.info("Staff profile created for user: {}", user.getUsername());
            }
            case ADMIN -> {
                // Admin không cần profile riêng
                log.info("Admin user created: {}", user.getUsername());
            }
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Method để lấy thông tin đầy đủ của user
    public String getFullUserInfo(User user) {
        // TODO: Implement logic để lấy thông tin từ Customer/Staff entity
        // Hiện tại chỉ trả về username
        return user.getUsername();
    }
}

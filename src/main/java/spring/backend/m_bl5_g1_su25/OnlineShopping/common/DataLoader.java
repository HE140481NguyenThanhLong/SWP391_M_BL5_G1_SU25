package spring.backend.m_bl5_g1_su25.OnlineShopping.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.StaffRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Starting data initialization...");

        createTestAccounts();

        log.info("Data initialization completed!");
    }

    private void createTestAccounts() {
        // Test Account 1: Admin
        createTestUser(
            "admin",
            "admin@test.com",
            "123456",
            User.Role.ADMIN,
            "Admin",
            "User",
            "0123456789"
        );

        // Test Account 2: Customer
        createTestUser(
            "customer",
            "customer@test.com",
            "123456",
            User.Role.CUSTOMER,
            "Test",
            "Customer",
            "0987654321"
        );

        // Test Account 3: Staff
        createTestUser(
            "staff",
            "staff@test.com",
            "123456",
            User.Role.STAFF,
            "Test",
            "Staff",
            "0555666777"
        );
    }

    private void createTestUser(String username, String email, String rawPassword,
                               User.Role role, String firstname, String lastname, String phoneNumber) {

        // Kiểm tra xem user đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            log.info("User with email {} already exists, skipping...", email);
            return;
        }

        try {
            // Tạo User entity với password được encode
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword)) // Encode password
                    .role(role)
                    .status(User.Status.ACTIVE)
                    .isDeleted(false)
                    .build();

            User savedUser = userRepository.save(user);
            log.info("Created test user: {} ({})", savedUser.getUsername(), savedUser.getEmail());

            // Tạo Customer hoặc Staff profile tương ứng
            createUserProfile(savedUser, firstname, lastname, phoneNumber);

        } catch (java.lang.Exception e) {
            log.error("Error creating test user {}: {}", email, e.getMessage());
        }
    }

    private void createUserProfile(User user, String firstname, String lastname, String phoneNumber) {
        try {
            switch (user.getRole()) {
                case CUSTOMER -> {
                    // Với @MapsId, không cần set customer_id thủ công
                    Customer customer = Customer.builder()
                            .user(user)  // Chỉ cần set user, @MapsId sẽ tự động set ID
                            .firstname(firstname)
                            .lastname(lastname)
                            .phoneNumber(phoneNumber)
                            .build();
                    customerRepository.save(customer);
                    log.info("Created customer profile for: {}", user.getUsername());
                }
                case STAFF -> {
                    // Với @MapsId, không cần set staff_id thủ công
                    Staff staff = Staff.builder()
                            .user(user)  // Chỉ cần set user, @MapsId sẽ tự động set ID
                            .firstname(firstname)
                            .lastname(lastname)
                            .phoneNumber(phoneNumber)
                            .build();
                    staffRepository.save(staff);
                    log.info("Created staff profile for: {}", user.getUsername());
                }
                case ADMIN -> {
                    // Admin không cần profile riêng
                    log.info("Admin user created: {}", user.getUsername());
                }
            }
        } catch (java.lang.Exception e) {
            log.error("Error creating profile for user {}: {}", user.getUsername(), e.getMessage());
        }
    }
}

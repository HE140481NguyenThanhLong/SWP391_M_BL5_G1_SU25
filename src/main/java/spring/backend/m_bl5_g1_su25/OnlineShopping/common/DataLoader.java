package spring.backend.m_bl5_g1_su25.OnlineShopping.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void run(String... args) {
        log.info("Starting data initialization...");
        createTestAccounts();
        log.info("Data initialization completed!");
    }

    private void createTestAccounts() {
        // Test Admin Account
        createTestUser(
            "admin", "admin@test.com", "123456", User.Role.ADMIN,
            "John", "Wilson", "0123456789",
            "123 Admin Street, Ho Chi Minh City, Vietnam"
        );

        // Test Customer Account
        createTestUser(
            "customer", "customer@test.com", "123456", User.Role.CUSTOMER,
            "Alice", "Johnson", "0987654321",
            "456 Customer Avenue, Hanoi, Vietnam"
        );

        // Test Staff Account
        createTestUser(
            "staff", "staff@test.com", "123456", User.Role.STAFF,
            "Bob", "Thompson", "0555666777",
            "789 Staff Boulevard, Da Nang, Vietnam"
        );
    }

    protected void createTestUser(String username, String email, String password, User.Role role,
                               String firstname, String lastname, String phone, String address) {

        if (userRepository.existsByEmail(email)) {
            log.info("User with email {} already exists, skipping...", email);
            return;
        }

        try {
            // Create and save user with auto-generated username from firstname + lastname (with space) - preserve original case
            String autoUsername = firstname.trim() + " " + lastname.trim();

            User user = User.builder()
                .username(autoUsername) // Use auto-generated username preserving case
                .email(email)
                .password(passwordEncoder.encode(password))
                .address(address)
                .role(role)
                .status(User.Status.ACTIVE)
                .isDeleted(false)
                .build();

            User savedUser = userRepository.save(user);
            userRepository.flush(); // Force immediate persistence
            log.info("Created test user: {} ({}) with auto-generated username from: {} {}",
                     savedUser.getUsername(), savedUser.getEmail(), firstname, lastname);

            // Create user profile based on role - ensure User is persisted first
            switch (savedUser.getRole()) {
                case CUSTOMER -> {
                    Customer customer = Customer.builder()
                        .user(savedUser) // Don't set customer_id explicitly - @MapsId will handle it
                        .firstname(firstname)
                        .lastname(lastname)
                        .phoneNumber(phone)
                        .build();
                    Customer savedCustomer = customerRepository.save(customer);
                    customerRepository.flush(); // Force immediate persistence
                    log.info("Created customer profile for: {} - ID: {}, FirstName: {}, LastName: {}",
                             savedUser.getUsername(), savedCustomer.getCustomer_id(),
                             savedCustomer.getFirstname(), savedCustomer.getLastname());
                }
                case STAFF -> {
                    Staff staff = Staff.builder()
                        .user(savedUser) // Don't set staff_id explicitly - @MapsId will handle it
                        .firstname(firstname)
                        .lastname(lastname)
                        .phoneNumber(phone)
                        .build();
                    Staff savedStaff = staffRepository.save(staff);
                    staffRepository.flush(); // Force immediate persistence
                    log.info("Created staff profile for: {} - ID: {}, FirstName: {}, LastName: {}",
                             savedUser.getUsername(), savedStaff.getStaff_id(),
                             savedStaff.getFirstname(), savedStaff.getLastname());
                }
                case ADMIN -> {
                    log.info("Admin user created: {} - No profile needed", savedUser.getUsername());
                }
            }

        } catch (java.lang.Exception e) {
            log.error("Error creating test user {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to create test user: " + email, e);
        }
    }
}

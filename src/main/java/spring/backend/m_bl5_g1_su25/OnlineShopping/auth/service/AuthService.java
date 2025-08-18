package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.dto.RegisterRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.service.EmailService;
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
    private final EmailService emailService;

    /**
     * Register a new user account
     * @param request Registration data containing user information
     * @return Created user entity
     * @throws RuntimeException if email already exists
     */
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String username = request.getFirstname() + " " + request.getLastname();

        User user = User.builder()
                .username(username)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .role(request.getRole())
                .status(User.Status.ACTIVE)
                .isDeleted(false)
                .build();

        User savedUser = userRepository.save(user);
        createUserProfile(savedUser, request);

        try {
            emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
        } catch (Exception e) {
            log.warn("Failed to send welcome email to: {}", savedUser.getEmail());
        }

        log.info("User registered successfully: {} ({})", savedUser.getUsername(), savedUser.getEmail());
        return savedUser;
    }

    /**
     * Create appropriate user profile based on role (Customer/Staff/Admin)
     */
    private void createUserProfile(User user, RegisterRequest request) {
        switch (user.getRole()) {
            case CUSTOMER -> {
                Customer customer = Customer.builder()
                        .user(user)
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : "")
                        .build();
                customerRepository.save(customer);
                log.info("Customer profile created for user: {}", user.getUsername());
            }
            case STAFF -> {
                Staff staff = Staff.builder()
                        .user(user)
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : "")
                        .build();
                staffRepository.save(staff);
                log.info("Staff profile created for user: {}", user.getUsername());
            }
            case ADMIN -> {
                log.info("Admin user created: {}", user.getUsername());
            }
        }
    }

    /**
     * Find user by email address
     * @param email User's email
     * @return User entity
     * @throws RuntimeException if user not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Check if email already exists in database
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Get full user information
     * @param user User entity
     * @return String containing full user information
     */
    public String getFullUserInfo(User user) {
        return user.getUsername();
    }

    /**
     * Change user password
     * @param email User's email
     * @param newPassword New password to set
     */
    @Transactional
    public void changePassword(String email, String newPassword) {
        try {
            User user = findByEmail(email);
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            userRepository.save(user);
            log.info("Password changed successfully for user: {}", email);
        } catch (Exception e) {
            log.error("Failed to change password for user: {}. Error: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to change password. Please try again later.");
        }
    }

    /**
     * Validate password change request
     * @param email User's email
     * @param newPassword New password
     * @param confirmPassword Password confirmation
     * @throws RuntimeException if validation fails
     */
    public void validatePasswordChangeRequest(String email, String newPassword, String confirmPassword) {
        if (!existsByEmail(email)) {
            throw new RuntimeException("User not found with this email address");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password and confirmation password do not match");
        }

        if (newPassword.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
    }
}

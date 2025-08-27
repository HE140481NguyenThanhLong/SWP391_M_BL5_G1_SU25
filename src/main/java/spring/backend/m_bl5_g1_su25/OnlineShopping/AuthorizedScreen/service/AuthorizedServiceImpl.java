package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.ForgotPasswordRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.ResetPasswordRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.PasswordResetToken;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.PasswordResetTokenRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizedServiceImpl implements  AuthorizedService {
CustomerRepository customerRepository;
ModelMapper modelMapper;
AuthorizedRepo authorizedRepo;
PasswordEncoder passwordEncoder;
EmailService emailService;
PasswordResetTokenRepository passwordResetTokenRepository;

@Override
    public Customer signUp(SignUpRequest request) {
        User user = modelMapper.map(request, User.class);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = authorizedRepo.save(user);
        Customer customer = modelMapper.map(savedUser, Customer.class);
        customer.setUser(savedUser); // Link customer to user
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
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

@Override
public User findUserByEmail(String email) {
    return authorizedRepo.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public void sendPasswordResetToken(ForgotPasswordRequest request) {
        User user = findUserByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("Không tìm thấy tài khoản với email này");
        }
        passwordResetTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .used(false)
                .build();
        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user, token);
    }
    @Override
    public boolean isValidToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        Optional<PasswordResetToken> resetTokenOpt = passwordResetTokenRepository.findByToken(token);
        if (resetTokenOpt.isEmpty()) {
            return false;
        }
        PasswordResetToken resetToken = resetTokenOpt.get();
        return !resetToken.isUsed() && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }
    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return false;
        }
        Optional<PasswordResetToken> resetTokenOpt = passwordResetTokenRepository.findByToken(request.getToken());
        if (resetTokenOpt.isEmpty()) {
            return false;
        }
        PasswordResetToken resetToken = resetTokenOpt.get();
        if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authorizedRepo.save(user);
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
        return true;
    }
    public User findUserByUsername(String username) {
        return authorizedRepo.findByUsername(username).orElse(null);
    }
}

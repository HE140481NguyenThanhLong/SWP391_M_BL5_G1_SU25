package spring.backend.m_bl5_g1_su25.OnlineShopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký user mới
    public User registerUser(User user) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        // Mã hóa password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lưu user vào database
        return userRepository.save(user);
    }

    // Đăng nhập user bằng email
    public User loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Kiểm tra password
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }

        throw new RuntimeException("Invalid email or password!");
    }

    // Tìm user theo email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Cập nhật thông tin user
    public User save(User user) {
        return userRepository.save(user);
    }
}

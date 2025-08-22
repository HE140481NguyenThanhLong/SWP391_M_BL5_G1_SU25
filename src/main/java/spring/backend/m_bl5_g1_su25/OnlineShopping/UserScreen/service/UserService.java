package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.response.UserResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.mapper.UserMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<UserResponse> filterUsers(String username, Role role, UserStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<User> result;

        if (role != null && status != null) {
            result = userRepository.findByUsernameContainingIgnoreCaseAndRoleAndStatus(
                    username != null ? username : "",
                    role,
                    status,
                    pageable
            );
        } else if (role != null) {
            result = userRepository.findByUsernameContainingIgnoreCaseAndRole(
                    username != null ? username : "",
                    role,
                    pageable
            );
        } else if (status != null) {
            result = userRepository.findByUsernameContainingIgnoreCaseAndStatus(
                    username != null ? username : "",
                    status,
                    pageable
            );
        } else {
            result = userRepository.findByUsernameContainingIgnoreCase(
                    username != null ? username : "",
                    pageable
            );
        }

        return result.map(userMapper::toUserResponse);
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toUserResponse(user);
    }

    // Cập nhật role + status cho user
    public void updateUser(Integer id, Role role, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setRole(role);
        user.setStatus(status);

        userRepository.save(user);
    }
}

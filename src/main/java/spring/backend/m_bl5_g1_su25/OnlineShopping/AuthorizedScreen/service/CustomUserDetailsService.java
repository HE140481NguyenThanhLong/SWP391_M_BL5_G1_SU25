package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

import java.util.List;

/**
 * CustomUserDetailsService - Sử dụng Spring Security User thay vì custom UserPrincipal
 *
 * Phù hợp với approach: Login bằng username, Forget password là trang riêng dùng email
 * Spring Security User đủ đáp ứng nhu cầu authentication đơn giản
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthorizedRepo authorizedRepo;

    /**
     * Load user by username (không hỗ trợ email ở đây)
     * Forget password sẽ xử lý riêng bằng email ở trang khác
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authorizedRepo.findByUsername(username)
                .map(user -> {
                    // Kiểm tra user status để quyết định enable/disable
                    boolean isActive = user.getStatus() == UserStatus.ACTIVE;

                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                            .accountExpired(false)
                            .accountLocked(!isActive)        // Khóa nếu INACTIVE
                            .credentialsExpired(false)
                            .disabled(!isActive)             // Vô hiệu hóa nếu INACTIVE
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));
    }
}

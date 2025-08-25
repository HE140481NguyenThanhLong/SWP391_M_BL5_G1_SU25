package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findByUsernameContainingIgnoreCaseAndRoleAndStatus(
            String username, Role role, UserStatus status, Pageable pageable);

    Optional<User> findByUsername(String username);

    Page<User> findByUsernameContainingIgnoreCaseAndRole(
            String username, Role role, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndStatus(
            String username, UserStatus status, Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCase(
            String username, Pageable pageable);
}

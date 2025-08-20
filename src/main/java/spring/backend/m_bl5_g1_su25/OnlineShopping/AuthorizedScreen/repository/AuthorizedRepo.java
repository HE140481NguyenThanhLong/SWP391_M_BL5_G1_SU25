package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.Optional;

@Repository
public interface AuthorizedRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}

package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository;

import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.Optional;

@Repository
public interface AuthorizedRepo {
    Optional<User> findByUsername(String username);

}

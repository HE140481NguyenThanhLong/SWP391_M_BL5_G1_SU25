package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;


import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);

}

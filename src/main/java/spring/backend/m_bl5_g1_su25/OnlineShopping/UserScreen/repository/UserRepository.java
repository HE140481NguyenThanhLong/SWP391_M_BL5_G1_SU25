package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Không cần viết thêm, JpaRepository đã có sẵn findAll(), findById(), save(), deleteById()...
}

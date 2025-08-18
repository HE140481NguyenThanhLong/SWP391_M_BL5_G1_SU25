package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

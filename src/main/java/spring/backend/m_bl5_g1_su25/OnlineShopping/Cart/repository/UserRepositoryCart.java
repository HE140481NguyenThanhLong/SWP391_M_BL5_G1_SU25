package spring.backend.m_bl5_g1_su25.OnlineShopping.Cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.entity.User;


@Repository
public interface UserRepositoryCart extends JpaRepository<User, Integer> {
}

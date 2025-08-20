package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}

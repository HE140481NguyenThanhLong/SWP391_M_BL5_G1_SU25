package spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Customer có quan hệ OneToOne với User qua customer_id
}

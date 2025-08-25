package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("SELECT c FROM Customer c JOIN User u ON c.customer_id = u.user_id WHERE u.username = :username")
    Customer findCustomerByUsername(@Param("username") String username);
    @Query("SELECT CONCAT(c.firstname, ' ', c.lastname) FROM Customer c JOIN User u ON c.customer_id = u.user_id WHERE u.username = :username")
    String findCustomerNameByUsername(@Param("username") String username);

    Optional<Customer> findByUser(User user);
}

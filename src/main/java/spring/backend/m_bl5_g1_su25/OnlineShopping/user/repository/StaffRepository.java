package spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    // Staff có quan hệ OneToOne với User qua staff_id
}

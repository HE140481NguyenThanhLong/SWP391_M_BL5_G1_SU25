package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Query("SELECT s FROM Staff s JOIN User u ON s.staff_id= u.user_id WHERE u.user_id = :staffId")
    Staff findStaffByUserId(@Param("staffId") Integer staffId);

    Optional<Staff> findByUser(User user);
}

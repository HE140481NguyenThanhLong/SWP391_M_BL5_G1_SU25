package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.response.UserResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.mapper.UserMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EntityManager entityManager;

    public Page<UserResponse> filterUsers(String username, Role role, UserStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<User> result;

        if (role != null && status != null) {
            result = userRepository.findByUsernameContainingIgnoreCaseAndRoleAndStatus(
                    username != null ? username : "",
                    role,
                    status,
                    pageable
            );
        } else if (role != null) {
            result = userRepository.findByUsernameContainingIgnoreCaseAndRole(
                    username != null ? username : "",
                    role,
                    pageable
            );
        } else if (status != null) {
            result = userRepository.findByUsernameContainingIgnoreCaseAndStatus(
                    username != null ? username : "",
                    status,
                    pageable
            );
        } else {
            result = userRepository.findByUsernameContainingIgnoreCase(
                    username != null ? username : "",
                    pageable
            );
        }

        return result.map(userMapper::toUserResponse);
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void updateUserStatus(Integer id, UserStatus status) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        user.setStatus(status);
        entityManager.merge(user);
    }

    @Transactional
    public void updateUserRole(Integer id, Role newRole) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + id);
        }

        user.setRole(newRole);
        entityManager.merge(user);

        // Nếu đổi sang STAFF
        if (newRole == Role.STAFF) {
            Customer oldCustomer = entityManager.createQuery(
                            "SELECT c FROM Customer c WHERE c.user.user_id = :userId", Customer.class)
                    .setParameter("userId", user.getUser_id())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            String firstname = oldCustomer != null ? oldCustomer.getFirstname() : "N/A";
            String lastname = oldCustomer != null ? oldCustomer.getLastname() : "N/A";

            // Xóa Customer
            entityManager.createQuery("DELETE FROM Customer c WHERE c.user.user_id = :userId")
                    .setParameter("userId", user.getUser_id())
                    .executeUpdate();

            // Tạo Staff
            Staff staff = new Staff();
            staff.setUser(user);
            staff.setFirstname(firstname);
            staff.setLastname(lastname);

            entityManager.persist(staff);
        }

        // Nếu đổi sang CUSTOMER
        if (newRole == Role.CUSTOMER) {
            Staff oldStaff = entityManager.createQuery(
                            "SELECT s FROM Staff s WHERE s.user.user_id = :userId", Staff.class)
                    .setParameter("userId", user.getUser_id())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            String firstname = oldStaff != null ? oldStaff.getFirstname() : "N/A";
            String lastname = oldStaff != null ? oldStaff.getLastname() : "N/A";

            // Xóa Staff
            entityManager.createQuery("DELETE FROM Staff s WHERE s.user.user_id = :userId")
                    .setParameter("userId", user.getUser_id())
                    .executeUpdate();

            // Tạo Customer
            Customer customer = new Customer();
            customer.setUser(user);
            customer.setFirstname(firstname);
            customer.setLastname(lastname);

            entityManager.persist(customer);
        }
    }


}

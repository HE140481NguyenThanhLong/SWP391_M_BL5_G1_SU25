package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.ProfileViewDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.repository.StaffRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.mapper.ProfileMapper;

import java.util.Optional;

/**
 * ProfileService handles user profile data retrieval and mapping.
 *
 * Security Levels:
 * - STAFF and CUSTOMER: Same security level - both see basic info only (name, email, phone, role)
 * - ADMIN: Full access - sees all fields including administrative data (timestamps, status, etc.)
 *
 * The service fetches related data from Customer/Staff tables to get firstname/lastname,
 * then uses ProfileMapper to convert the data to ProfileViewDto with appropriate security filtering.
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ProfileMapper profileMapper;

    /**
     * Gets profile view data for a user with role-based security filtering.
     *
     * @param user The user whose profile to retrieve
     * @return ProfileViewDto with appropriate fields based on user's role/security level
     */
    public ProfileViewDto getProfileView(User user) {
        Customer customer = null;
        Staff staff = null;

        // Fetch related entities based on role to get firstname/lastname
        switch (user.getRole()) {
            case CUSTOMER:
                Optional<Customer> customerOpt = customerRepository.findByUser(user);
                customer = customerOpt.orElse(null);
                break;

            case STAFF:
            case ADMIN:
                Optional<Staff> staffOpt = staffRepository.findByUser(user);
                staff = staffOpt.orElse(null);
                break;
        }

        // Use mapper to convert User to ProfileViewDto with security filtering
        // STAFF and CUSTOMER will get basic info only, ADMIN gets full administrative details
        return profileMapper.mapUserToProfileView(user, customer, staff);
    }
}

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

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ProfileMapper profileMapper;
    public ProfileViewDto getProfileView(User user) {
        Customer customer = null;
        Staff staff = null;
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
        return profileMapper.mapUserToProfileView(user, customer, staff);
    }
}

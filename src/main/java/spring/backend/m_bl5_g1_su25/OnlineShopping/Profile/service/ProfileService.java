package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.ProfileViewDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.request.ProfileEditRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.mapper.ProfileMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.StaffRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final AuthorizedRepo userRepository;
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
                Optional<Staff> staffOpt = staffRepository.findByUser(user);
                staff = staffOpt.orElse(null);
                break;
        }
        return profileMapper.mapUserToProfileView(user, customer, staff);
    }

    @Transactional
    public boolean updateProfile(User currentUser, ProfileEditRequest request) {
        if (isUsernameExists(request.getUsername(), currentUser.getUser_id())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        if (isEmailExists(request.getEmail(), currentUser.getUser_id())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        currentUser.setUsername(request.getUsername());
        currentUser.setEmail(request.getEmail());
        currentUser.setPhoneNumber(request.getPhoneNumber());
        currentUser.setAddress1(request.getAddress1());
        currentUser.setAddress2(request.getAddress2());
        userRepository.save(currentUser);

        Customer customer = customerRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin customer"));

        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
        customerRepository.save(customer);

        return true;
    }

    private boolean isUsernameExists(String username, Integer excludeUserId) {
        return userRepository.findByUsername(username)
                .map(user -> !user.getUser_id().equals(excludeUserId))
                .orElse(false);
    }

    private boolean isEmailExists(String email, Integer excludeUserId) {
        return userRepository.findByEmail(email)
                .map(user -> !user.getUser_id().equals(excludeUserId))
                .orElse(false);
    }
}

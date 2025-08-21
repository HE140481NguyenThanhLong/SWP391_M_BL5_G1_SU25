package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.repository.AuthorizedRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.repository.StaffRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;

/**
 * GlobalControllerAdvice - Tự động inject thông tin user vào header
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerAdvice {

    private final AuthorizedRepo authorizedRepo;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @ModelAttribute
    public void addUserInfoToModel(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

                String username = authentication.getName();
                log.debug("Adding user info for username: {}", username);

                // Lấy thông tin User từ database
                User user = authorizedRepo.findByUsername(username).orElse(null);

                if (user != null) {
                    model.addAttribute("currentUser", user);

                    // Lấy tên đầy đủ dựa vào role
                    String fullName = getFullName(user);
                    model.addAttribute("currentUserFullName", fullName);
                    model.addAttribute("currentUserEmail", user.getEmail());
                    model.addAttribute("currentUserRole", user.getRole().name());

                    log.debug("Added user info: {} - {} ({})", fullName, user.getEmail(), user.getRole());
                } else {
                    log.warn("User not found in database: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("Error adding user info to model: {}", e.getMessage(), e);
        }
    }

    /**
     * Lấy tên đầy đủ của user dựa vào role
     */
    private String getFullName(User user) {
        try {
            if (user.getRole() == Role.CUSTOMER) {
                Customer customer = customerRepository.findByUser(user).orElse(null);
                if (customer != null) {
                    return customer.getFirstname() + " " + customer.getLastname();
                }
            } else if (user.getRole() == Role.STAFF) {
                Staff staff = staffRepository.findByUser(user).orElse(null);
                if (staff != null) {
                    return staff.getFirstname() + " " + staff.getLastname();
                }
            }
            // Fallback cho ADMIN hoặc khi không tìm thấy profile
            return user.getUsername();
        } catch (Exception e) {
            log.error("Error getting full name for user {}: {}", user.getUsername(), e.getMessage());
            return user.getUsername();
        }
    }
}

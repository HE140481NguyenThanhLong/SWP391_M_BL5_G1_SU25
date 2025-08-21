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

import jakarta.servlet.http.HttpServletRequest;

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
    public void addUserInfoToModel(Model model, HttpServletRequest request) {
        try {
            // Chỉ thực hiện cho các request không phải AJAX hoặc API
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/api/") || requestURI.contains(".js") ||
                requestURI.contains(".css") || requestURI.contains(".ico")) {
                return;
            }

            // Kiểm tra xem đã có thông tin user trong model chưa
            if (model.containsAttribute("currentUser")) {
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

                String username = authentication.getName();

                // Lấy thông tin User từ database
                User user = authorizedRepo.findByUsername(username).orElse(null);

                if (user != null) {
                    model.addAttribute("currentUser", user);
                    model.addAttribute("currentUserFullName", getFullName(user));
                    model.addAttribute("currentUserEmail", user.getEmail());
                    model.addAttribute("currentUserRole", user.getRole().name());

                    log.debug("Added user info: {} - {} ({})", getFullName(user), user.getEmail(), user.getRole());
                }
            }
        } catch (Exception e) {
            log.error("Error adding user info to model: {}", e.getMessage());
        }
    }

    private String getFullName(User user) {
        try {
            if (user.getRole() == Role.CUSTOMER) {
                Customer customer = customerRepository.findByUser(user).orElse(null);
                return customer != null ? customer.getFirstname() + " " + customer.getLastname() : user.getUsername();
            } else if (user.getRole() == Role.STAFF) {
                Staff staff = staffRepository.findByUser(user).orElse(null);
                return staff != null ? staff.getFirstname() + " " + staff.getLastname() : user.getUsername();
            }
            return user.getUsername();
        } catch (Exception e) {
            log.error("Error getting full name for user {}: {}", user.getUsername(), e.getMessage());
            return user.getUsername();
        }
    }
}

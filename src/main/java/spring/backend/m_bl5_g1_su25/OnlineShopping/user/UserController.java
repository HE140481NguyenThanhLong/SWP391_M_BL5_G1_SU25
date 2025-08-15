package spring.backend.m_bl5_g1_su25.OnlineShopping.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.AuthService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.StaffRepository;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @GetMapping({"/customer/profile", "/staff/profile"})
    public String showProfile(Authentication authentication, Model model) {
        User user = authService.findByEmail(authentication.getName());

        model.addAttribute("user", user);

        // Load Customer/Staff profile based on role
        if (user.getRole() == User.Role.CUSTOMER) {
            Optional<Customer> customerOpt = customerRepository.findById(user.getUser_id());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                model.addAttribute("profile", customer);
                model.addAttribute("firstname", customer.getFirstname()); // Correct getter
                model.addAttribute("lastname", customer.getLastname());   // Correct getter
                model.addAttribute("phoneNumber", customer.getPhoneNumber()); // Correct getter
            }
        } else if (user.getRole() == User.Role.STAFF) {
            Optional<Staff> staffOpt = staffRepository.findById(user.getUser_id());
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                model.addAttribute("profile", staff);
                model.addAttribute("firstname", staff.getFirstname()); // Correct getter
                model.addAttribute("lastname", staff.getLastname());   // Correct getter
                model.addAttribute("phoneNumber", staff.getPhoneNumber()); // Correct getter
            }
        }

        return "shared/profile";
    }

    @PostMapping({"/customer/profile/update", "/staff/profile/update"})
    public String updateProfile(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam(value = "firstname", required = false) String firstname,
                               @RequestParam(value = "lastname", required = false) String lastname,
                               @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // Validation
        StringBuilder errors = new StringBuilder();

        if (username == null || username.trim().length() < 2 || username.trim().length() > 50) {
            errors.append("Username must be between 2 and 50 characters; ");
        }

        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("Please provide a valid email address; ");
        }

        if (firstname != null && (firstname.trim().length() < 2 || firstname.trim().length() > 50)) {
            errors.append("First name must be between 2 and 50 characters; ");
        }

        if (lastname != null && (lastname.trim().length() < 2 || lastname.trim().length() > 50)) {
            errors.append("Last name must be between 2 and 50 characters; ");
        }

        if (phoneNumber != null && (phoneNumber.trim().length() < 10 || phoneNumber.trim().length() > 15)) {
            errors.append("Phone number must be between 10 and 15 characters; ");
        }

        if (!errors.isEmpty()) {
            User currentUser = authService.findByEmail(authentication.getName());
            model.addAttribute("user", currentUser);
            model.addAttribute("error", "Please fix the following errors: " + errors.toString());
            return "shared/profile";
        }

        try {
            // Update User entity
            User currentUser = authService.findByEmail(authentication.getName());

            // Check if email changed and already exists
            if (!currentUser.getEmail().equals(email.trim()) && authService.existsByEmail(email.trim())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists");
                return getRedirectPath(authentication);
            }

            currentUser.setUsername(username.trim());
            currentUser.setEmail(email.trim());
            userRepository.save(currentUser);

            // Update Customer/Staff profile
            if (currentUser.getRole() == User.Role.CUSTOMER && firstname != null && lastname != null && phoneNumber != null) {
                Optional<Customer> customerOpt = customerRepository.findById(currentUser.getUser_id());
                if (customerOpt.isPresent()) {
                    Customer customer = customerOpt.get();
                    customer.setFirstname(firstname.trim());
                    customer.setLastname(lastname.trim());
                    customer.setPhoneNumber(phoneNumber.trim());
                    customerRepository.save(customer);
                }
            } else if (currentUser.getRole() == User.Role.STAFF && firstname != null && lastname != null && phoneNumber != null) {
                Optional<Staff> staffOpt = staffRepository.findById(currentUser.getUser_id());
                if (staffOpt.isPresent()) {
                    Staff staff = staffOpt.get();
                    staff.setFirstname(firstname.trim());
                    staff.setLastname(lastname.trim());
                    staff.setPhoneNumber(phoneNumber.trim());
                    staffRepository.save(staff);
                }
            }

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return getRedirectPath(authentication);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return getRedirectPath(authentication);
        }
    }

    private String getRedirectPath(Authentication authentication) {
        User user = authService.findByEmail(authentication.getName());
        return switch (user.getRole()) {
            case CUSTOMER -> "redirect:/customer/profile";
            case STAFF -> "redirect:/staff/profile";
            default -> "redirect:/dashboard";
        };
    }
}

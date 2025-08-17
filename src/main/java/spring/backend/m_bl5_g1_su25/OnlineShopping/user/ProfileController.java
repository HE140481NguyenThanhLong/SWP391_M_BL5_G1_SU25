package spring.backend.m_bl5_g1_su25.OnlineShopping.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.AuthService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.repository.UserRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.repository.StaffRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.dto.ProfileUpdateRequest;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @GetMapping({"/customer/profile/edit", "/staff/profile/edit"})
    public String showEditProfile(Authentication authentication, Model model) {
        User user = authService.findByEmail(authentication.getName());
        model.addAttribute("user", user);

        // Create ProfileUpdateRequest with current user data
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        profileUpdateRequest.setUsername(user.getUsername());
        profileUpdateRequest.setEmail(user.getEmail());
        profileUpdateRequest.setAddress(user.getAddress());

        // Load Customer/Staff profile based on role
        if (user.getRole() == User.Role.CUSTOMER) {
            Optional<Customer> customerOpt = customerRepository.findById(user.getUser_id());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                model.addAttribute("profile", customer);
                model.addAttribute("firstname", customer.getFirstname());
                model.addAttribute("lastname", customer.getLastname());
                model.addAttribute("phoneNumber", customer.getPhoneNumber());

                profileUpdateRequest.setFirstname(customer.getFirstname());
                profileUpdateRequest.setLastname(customer.getLastname());
                profileUpdateRequest.setPhoneNumber(customer.getPhoneNumber());
            }
        } else if (user.getRole() == User.Role.STAFF) {
            Optional<Staff> staffOpt = staffRepository.findById(user.getUser_id());
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                model.addAttribute("profile", staff);
                model.addAttribute("firstname", staff.getFirstname());
                model.addAttribute("lastname", staff.getLastname());
                model.addAttribute("phoneNumber", staff.getPhoneNumber());

                profileUpdateRequest.setFirstname(staff.getFirstname());
                profileUpdateRequest.setLastname(staff.getLastname());
                profileUpdateRequest.setPhoneNumber(staff.getPhoneNumber());
            }
        }

        model.addAttribute("profileUpdateRequest", profileUpdateRequest);
        return "shared/edit-profile";
    }

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
                model.addAttribute("firstname", customer.getFirstname());
                model.addAttribute("lastname", customer.getLastname());
                model.addAttribute("phoneNumber", customer.getPhoneNumber());
            }
        } else if (user.getRole() == User.Role.STAFF) {
            Optional<Staff> staffOpt = staffRepository.findById(user.getUser_id());
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                model.addAttribute("profile", staff);
                model.addAttribute("firstname", staff.getFirstname());
                model.addAttribute("lastname", staff.getLastname());
                model.addAttribute("phoneNumber", staff.getPhoneNumber());
            }
        }

        return "shared/profile";
    }

    @PostMapping({"/customer/profile/update", "/staff/profile/update"})
    public String updateProfile(@Valid @ModelAttribute ProfileUpdateRequest profileUpdateRequest,
                               BindingResult bindingResult,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        User currentUser = authService.findByEmail(authentication.getName());

        // Check if email changed and already exists
        if (!currentUser.getEmail().equals(profileUpdateRequest.getEmail().trim())
            && authService.existsByEmail(profileUpdateRequest.getEmail().trim())) {
            bindingResult.rejectValue("email", "error.email", "Email already exists");
        }

        // If there are validation errors, return to edit form
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", currentUser);

            // Load profile data for display in sidebar
            if (currentUser.getRole() == User.Role.CUSTOMER) {
                Optional<Customer> customerOpt = customerRepository.findById(currentUser.getUser_id());
                if (customerOpt.isPresent()) {
                    Customer customer = customerOpt.get();
                    model.addAttribute("profile", customer);
                    model.addAttribute("firstname", customer.getFirstname());
                    model.addAttribute("lastname", customer.getLastname());
                    model.addAttribute("phoneNumber", customer.getPhoneNumber());
                }
            } else if (currentUser.getRole() == User.Role.STAFF) {
                Optional<Staff> staffOpt = staffRepository.findById(currentUser.getUser_id());
                if (staffOpt.isPresent()) {
                    Staff staff = staffOpt.get();
                    model.addAttribute("profile", staff);
                    model.addAttribute("firstname", staff.getFirstname());
                    model.addAttribute("lastname", staff.getLastname());
                    model.addAttribute("phoneNumber", staff.getPhoneNumber());
                }
            }

            // Return to edit form with errors
            return "shared/edit-profile";
        }

        try {
            // For Customer/Staff: Auto-generate username from firstname + lastname (with space) - preserve original case
            if (currentUser.getRole() != User.Role.ADMIN) {
                String autoUsername = profileUpdateRequest.getFirstname().trim() + " " + profileUpdateRequest.getLastname().trim();
                currentUser.setUsername(autoUsername);
            } else {
                // For ADMIN, use provided username
                currentUser.setUsername(profileUpdateRequest.getUsername().trim());
            }

            currentUser.setEmail(profileUpdateRequest.getEmail().trim());
            currentUser.setAddress(profileUpdateRequest.getAddress().trim());
            userRepository.save(currentUser);

            // Update Customer/Staff profile
            if (currentUser.getRole() == User.Role.CUSTOMER) {
                Optional<Customer> customerOpt = customerRepository.findById(currentUser.getUser_id());
                if (customerOpt.isPresent()) {
                    Customer customer = customerOpt.get();
                    customer.setFirstname(profileUpdateRequest.getFirstname().trim());
                    customer.setLastname(profileUpdateRequest.getLastname().trim());
                    customer.setPhoneNumber(profileUpdateRequest.getPhoneNumber().trim());
                    customerRepository.save(customer);
                }
            } else if (currentUser.getRole() == User.Role.STAFF) {
                Optional<Staff> staffOpt = staffRepository.findById(currentUser.getUser_id());
                if (staffOpt.isPresent()) {
                    Staff staff = staffOpt.get();
                    staff.setFirstname(profileUpdateRequest.getFirstname().trim());
                    staff.setLastname(profileUpdateRequest.getLastname().trim());
                    staff.setPhoneNumber(profileUpdateRequest.getPhoneNumber().trim());
                    staffRepository.save(staff);
                }
            }

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
            return getRedirectPath(authentication);

        } catch (Exception e) {
            log.error("Error updating profile for user {}: {}", currentUser.getEmail(), e.getMessage(), e);
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

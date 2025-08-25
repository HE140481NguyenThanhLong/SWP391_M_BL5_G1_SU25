package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignInRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.AuthorizedService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizedController {

    private final AuthorizedService authorizedService;

    @Autowired
    public AuthorizedController(AuthorizedService authorizedService) {
        this.authorizedService = authorizedService;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "homeScreen/Home";
        }
        if (loggedInUser.getRole() == Role.STAFF) {
            return "redirect:/staff/staff-dashboard";
        } else if (loggedInUser.getRole() == Role.CUSTOMER) {
            return "homeScreen/Home";
        } else {
            return "homeScreen/Home";
        }
    }

    @GetMapping("/authority/signup")
    public String showSignUp(Model model) {
        model.addAttribute("SignUprequest", new SignUpRequest());
        return "/authority/signup";
    }

    @GetMapping("/authority/signin")
    public String showLogin(Model model) {
        model.addAttribute("SignInRequest", new SignInRequest());
        return "/authority/signin";
    }

    @PostMapping("/authority/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/authority/signin?logout";
    }

    @PostMapping("/authority/signin")
    public String doLogin(@ModelAttribute SignInRequest SignInrequest, Model model, HttpSession session) {
        try {
            // Validate input
            if (SignInrequest.getUsername() == null || SignInrequest.getUsername().trim().isEmpty()) {
                model.addAttribute("error", "Tên đăng nhập không được để trống");
                model.addAttribute("SignInRequest", new SignInRequest());
                return "authority/signin";
            }

            if (SignInrequest.getPassword() == null || SignInrequest.getPassword().trim().isEmpty()) {
                model.addAttribute("error", "Mật khẩu không được để trống");
                model.addAttribute("SignInRequest", new SignInRequest());
                return "authority/signin";
            }

            User user = authorizedService.login(SignInrequest.getUsername().trim(), SignInrequest.getPassword());
            if (user != null) {
                if(user.getStatus() == UserStatus.INACTIVE){
                    model.addAttribute("error", "Tài khoản không được phép hoạt động");
                    model.addAttribute("SignInRequest", new SignInRequest());
                    return "authority/signin";
                }
                session.setAttribute("loggedInUser", user);
                if(user.getRole() == Role.STAFF){
                    return "redirect:/staff/staff-dashboard";
                }
                return "redirect:/guest";
            } else {
                model.addAttribute("error","Tài khoản hoặc mật khẩu không đúng");
                model.addAttribute("SignInRequest", new SignInRequest());
                return "authority/signin";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình đăng nhập. Vui lòng thử lại.");
            model.addAttribute("SignInRequest", new SignInRequest());
            return "authority/signin";
        }
    }

    @PostMapping("/authority/signup")
    public String signUp(@ModelAttribute SignUpRequest SignUprequest, Model model) {
        try{
            if (SignUprequest.getUsername() == null || SignUprequest.getUsername().trim().isEmpty()) {
                model.addAttribute("error", "Tên đăng nhập không được để trống");
                model.addAttribute("SignUprequest", SignUprequest);
                return "authority/signup";
            }

            if (SignUprequest.getPassword() == null || SignUprequest.getPassword().length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
                model.addAttribute("SignUprequest", SignUprequest);
                return "authority/signup";
            }

            if (SignUprequest.getEmail() == null || !SignUprequest.getEmail().contains("@")) {
                model.addAttribute("error", "Email không hợp lệ");
                model.addAttribute("SignUprequest", SignUprequest);
                return "authority/signup";
            }

            SignUprequest.setUsername(SignUprequest.getUsername().trim());//xóa khoảng trắng

            if (authorizedService.findUserByUsername(SignUprequest.getUsername()) != null) {
                model.addAttribute("error", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên đăng nhập khác.");
                model.addAttribute("SignUprequest", SignUprequest);
                return "authority/signup";
            }

            authorizedService.signUp(SignUprequest);
            return "redirect:/authority/signin?success";
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký không thành công: " + e.getMessage());
            model.addAttribute("SignUprequest", SignUprequest);
            return "authority/signup";
        }
    }
}

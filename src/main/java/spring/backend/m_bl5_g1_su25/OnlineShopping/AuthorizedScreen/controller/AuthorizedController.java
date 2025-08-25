package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.ForgotPasswordRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.ResetPasswordRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignInRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.AuthorizedService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.AuthorizedServiceImpl;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizedController {

    private final AuthorizedServiceImpl authorizedService;

    @Autowired
    public AuthorizedController(AuthorizedServiceImpl authorizedService) {
        this.authorizedService = authorizedService;
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
    public String doLogin(@ModelAttribute SignInRequest SignInrequest, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (SignInrequest.getUsername() == null || SignInrequest.getUsername().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập không được để trống");
                return "redirect:/authority/signin";
            }

            if (SignInrequest.getPassword() == null || SignInrequest.getPassword().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không được để trống");
                return "redirect:/authority/signin";
            }
            User user = authorizedService.login(SignInrequest.getUsername().trim(), SignInrequest.getPassword());
            if (user != null) {
                if(user.getStatus() == UserStatus.INACTIVE){
                    redirectAttributes.addFlashAttribute("error", "Tài khoản không được phép hoạt động");
                    return "redirect:/authority/signin";
                }
                session.setAttribute("loggedInUser", user);
                if(user.getRole() == Role.STAFF){
                    return "redirect:/staff/staff-dashboard";
                }
                return "redirect:/guest";
            } else {
                redirectAttributes.addFlashAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
                return "redirect:/authority/signin";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi trong quá trình đăng nhập. Vui lòng thử lại.");
            return "redirect:/authority/signin";
        }
    }

    @GetMapping("/authority/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        return "/authority/forgot-password";
    }

    @PostMapping("/authority/forgot-password")
    public String processForgotPassword(@Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequest request,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/authority/forgot-password";
        }

        try {
            authorizedService.sendPasswordResetToken(request);
            redirectAttributes.addFlashAttribute("successMessage",
                "Đã gửi liên kết đặt lại mật khẩu đến email của bạn. Vui lòng kiểm tra email và làm theo hướng dẫn.");
            return "redirect:/authority/signin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/authority/forgot-password";
        }
    }
    @GetMapping("/authority/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!authorizedService.isValidToken(token)) {
            model.addAttribute("error", "Liên kết đặt lại mật khẩu không hợp lệ hoặc đã hết hạn");
            return "/authority/signin";
        }
        ResetPasswordRequest resetRequest = new ResetPasswordRequest();
        resetRequest.setToken(token);
        model.addAttribute("resetPasswordRequest", resetRequest);
        return "/authority/reset-password";
    }

    @PostMapping("/authority/reset-password")
    public String processResetPassword(@Valid @ModelAttribute("resetPasswordRequest") ResetPasswordRequest request,
                                      BindingResult bindingResult,
                                      HttpSession session,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/authority/reset-password";
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            return "/authority/reset-password";
        }

        try {
            boolean success = authorizedService.resetPassword(request);
            if (success) {
                session.invalidate();
                redirectAttributes.addFlashAttribute("successMessage",
                    "Đặt lại mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
                return "redirect:/authority/signin";
            } else {
                model.addAttribute("error", "Liên kết đặt lại mật kh��u không hợp lệ hoặc đã hết hạn");
                return "/authority/reset-password";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi đặt lại mật khẩu: " + e.getMessage());
            return "/authority/reset-password";
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

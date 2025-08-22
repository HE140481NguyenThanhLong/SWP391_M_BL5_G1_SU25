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
import org.springframework.web.bind.annotation.RequestMapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignInRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request.SignUpRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.AuthorizedService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/authority")
public class AuthorizedController {

    private final      AuthorizedService authorizedService;
    CustomerRepository customerRepository;
    @Autowired
    public AuthorizedController(AuthorizedService authorizedService) {
        this.authorizedService = authorizedService;
    }

    @GetMapping("/signup")
    public String showSignUp(Model model) {
        model.addAttribute("SignUprequest", new SignUpRequest());
        return  "/authority/signup";
    }
    @GetMapping("/signin")
    public String showLogin(){return  "/authority/signin";}
    @PostMapping("/signup")
    public String signUp(@ModelAttribute SignUpRequest SignUprequest, Model model) {
        try{
            Customer signUpCus= authorizedService.signUp(SignUprequest);
            return "redirect:/authority/signin";
        } catch (Exception e) {
            return "redirect:/authority/signup";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "/authority/signin";

    }
    @PostMapping("/signin")
    public String doLogin(@ModelAttribute SignInRequest SignInrequest, Model model, HttpSession session) {
        User user = authorizedService.login(SignInrequest.getUsername(), SignInrequest.getPassword());
        if (user != null) {
            if(user.getStatus()== UserStatus.INACTIVE){
                model.addAttribute("error", "Tài khoản không được phép hoạt động");
                return "/authority/signin";
            }
            session.setAttribute("loggedInUser", user);
            return "redirect:/guest";

        }else{
            model.addAttribute("error","Tài khoản hoặc mật khẩu không đúng");
            return "/authority/signin";
        }

    }


}

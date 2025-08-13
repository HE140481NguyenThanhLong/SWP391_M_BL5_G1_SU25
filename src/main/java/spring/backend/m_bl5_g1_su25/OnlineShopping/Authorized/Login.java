package spring.backend.m_bl5_g1_su25.OnlineShopping.Authorized;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Login {

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}

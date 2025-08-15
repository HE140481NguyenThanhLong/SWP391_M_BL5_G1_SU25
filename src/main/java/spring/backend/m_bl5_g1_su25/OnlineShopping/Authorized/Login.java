package spring.backend.m_bl5_g1_su25.OnlineShopping.Authorized;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class Login implements AuthenticationSuccessHandler {

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

        // Get user role from authentication
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // Redirect based on user role (only STAFF and CUSTOMER)
        String redirectUrl = switch (role) {
            case "ROLE_STAFF" -> "/staff/dashboard";
            case "ROLE_CUSTOMER" -> "/dashboard";
            default -> "/dashboard"; // Default to public dashboard
        };

        // Simple redirect without context path
        response.sendRedirect(redirectUrl);
    }
}

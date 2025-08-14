package spring.backend.m_bl5_g1_su25.OnlineShopping.Authorized;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Logout implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                               HttpServletResponse response,
                               Authentication authentication) throws IOException {

        // Clear any additional session data if needed
        // Log logout activity if required

        // Redirect to login page with logout success message
        response.sendRedirect("/login?logout=true");
    }
}

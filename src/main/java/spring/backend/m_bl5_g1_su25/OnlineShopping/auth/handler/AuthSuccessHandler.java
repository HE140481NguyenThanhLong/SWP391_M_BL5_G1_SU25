package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

        // Get User role from authorities
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        // Redirect based on role
        String redirectUrl = switch (role) {
            case "CUSTOMER" -> "/customer/dashboard";
            case "STAFF" -> "/staff/dashboard";
            default -> "/guest"; // Redirect to guest home instead of /dashboard
        };

        response.sendRedirect(redirectUrl);
    }
}

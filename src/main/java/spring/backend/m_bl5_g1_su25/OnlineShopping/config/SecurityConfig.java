package spring.backend.m_bl5_g1_su25.OnlineShopping.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.backend.m_bl5_g1_su25.OnlineShopping.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Public access
                .requestMatchers("/signup", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/", "/dashboard").permitAll()
                .requestMatchers("/notifications/**").permitAll()

                // Role-based access control
                .requestMatchers("/staff/**").hasRole("STAFF")
                .requestMatchers("/deliverer/**").hasRole("DELIVERER")
                .requestMatchers("/customer/**").hasRole("CUSTOMER")

                // Profile access for authenticated users
                .requestMatchers("/profile").authenticated()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username") // Email is used as username
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    // Redirect based on user role after successful login
                    String role = authentication.getAuthorities().iterator().next().getAuthority();
                    switch (role) {
                        case "ROLE_STAFF":
                            response.sendRedirect("/staff/dashboard");
                            break;
                        case "ROLE_DELIVERER":
                            response.sendRedirect("/deliverer/dashboard");
                            break;
                        case "ROLE_CUSTOMER":
                            response.sendRedirect("/dashboard");
                            break;
                        default:
                            response.sendRedirect("/dashboard");
                            break;
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .userDetailsService(userDetailsService)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

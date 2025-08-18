package spring.backend.m_bl5_g1_su25.OnlineShopping.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.service.CustomUserDetailsService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.handler.AuthSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final AuthSuccessHandler authSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws java.lang.Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login", "/signup", "/dashboard", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/shared/change-password/**").permitAll()
                .requestMatchers("/shared/forgot-password/**", "/shared/reset-password/**").permitAll()
                .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                .requestMatchers("/staff/**").hasAuthority("STAFF")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authSuccessHandler)
                .failureUrl("/login?error")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("smartshop-remember-me-key")
                .tokenValiditySeconds(30 * 24 * 60 * 60) // 30 days
                .userDetailsService(userDetailsService)
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("smartshop-remember-me")
            )
            .userDetailsService(userDetailsService)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "smartshop-remember-me")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .and()
                .sessionFixation().changeSessionId()
                .invalidSessionUrl("/login?expired")
            );

        return http.build();
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.service.PasswordResetService;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final PasswordResetService passwordResetService;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        passwordResetService.cleanupExpiredTokens();
    }
}

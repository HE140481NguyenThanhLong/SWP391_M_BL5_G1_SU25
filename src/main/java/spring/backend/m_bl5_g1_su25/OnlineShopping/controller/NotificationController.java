package spring.backend.m_bl5_g1_su25.OnlineShopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @GetMapping("/content")
    public String getNotificationContent() {
        return "shared/notification-content";
    }
}

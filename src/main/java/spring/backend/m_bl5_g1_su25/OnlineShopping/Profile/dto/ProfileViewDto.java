package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProfileViewDto {
    private Integer userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String address1;
    private String address2;
    private String role;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean showAdminFields = false;
}

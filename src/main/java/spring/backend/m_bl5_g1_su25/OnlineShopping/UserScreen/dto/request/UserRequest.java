package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.request;

import lombok.Data;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

@Data
public class UserRequest {
    private Role role;
    private UserStatus status;
}

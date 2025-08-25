package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    String firstname;
    String lastname;
    String phone_number;
    String email;
    Role role;
    LocalDateTime created_at;
    LocalDateTime updated_at;
    String address;
    UserStatus status;
    String gender;
    Integer user_id;
    String username;
    String phoneNumber;

}

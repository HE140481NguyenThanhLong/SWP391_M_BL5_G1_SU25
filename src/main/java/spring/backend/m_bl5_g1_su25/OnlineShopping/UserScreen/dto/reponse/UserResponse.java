package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.reponse;


import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.UserStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {

     Integer user_id;
     String username,email,phoneNumber;
     LocalDateTime created_at,updated_at;
     Role role;
     UserStatus status;


}

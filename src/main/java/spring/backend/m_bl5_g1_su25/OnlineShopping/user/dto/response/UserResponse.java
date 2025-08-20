package spring.backend.m_bl5_g1_su25.OnlineShopping.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String role;
    LocalDateTime created_at;
    String address;      // lấy trực tiếp từ User
    LocalDate date_of_birth;
    String gender;
}

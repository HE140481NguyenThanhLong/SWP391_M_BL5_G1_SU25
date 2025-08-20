package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Password is required")
    String password;
    @NotBlank(message = "Email is requried")
    String email;
    @NotBlank(message = "Phone number is required")
    String phoneNumber;
}

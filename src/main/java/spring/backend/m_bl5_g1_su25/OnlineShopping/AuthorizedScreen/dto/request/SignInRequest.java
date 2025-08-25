package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SignInRequest {
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Password is required")
    String password;

}

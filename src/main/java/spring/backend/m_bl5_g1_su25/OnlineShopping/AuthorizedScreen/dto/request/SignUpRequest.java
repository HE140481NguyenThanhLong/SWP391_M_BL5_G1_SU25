package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    String firstname, lastname;
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Password is required")
    String password;
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Phone number is required")
    String phoneNumber;
    @NotNull(message = "Birthday is required")
    LocalDate birthday;
}

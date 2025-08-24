package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.UniqueEmail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.UniqueUsername;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.ValidPhoneNumber;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.ValidEmail;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.ValidName;

import java.time.LocalDate;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @UniqueUsername
    String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotBlank(message = "Email is required")
    @ValidEmail()
    @UniqueEmail
    String email;

    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber(message = "Phone number must be 10-11 digits")
    String phoneNumber;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    LocalDate birthday;

    @NotBlank(message = "First name is required")
    @ValidName(message = "First name must contain only letters, spaces, hyphens, and apostrophes")
    String firstname;

    @NotBlank(message = "Last name is required")
    @ValidName(message = "Last name must contain only letters, spaces, hyphens, and apostrophes")
    String lastname;
}

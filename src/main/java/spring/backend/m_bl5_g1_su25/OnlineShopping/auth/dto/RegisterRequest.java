package spring.backend.m_bl5_g1_su25.OnlineShopping.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation.ValidName;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation.ValidPhone;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @ValidName
    String firstname;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @ValidName
    String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotBlank(message = "Phone number is required")
    @ValidPhone
    String phoneNumber;

    @NotBlank(message = "Address is required")
    String address;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    User.Gender gender;

    User.Role role = User.Role.CUSTOMER;
}

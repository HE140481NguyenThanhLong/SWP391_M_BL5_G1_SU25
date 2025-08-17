package spring.backend.m_bl5_g1_su25.OnlineShopping.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation.ValidName;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation.ValidPhone;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @ValidName
    String firstname;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @ValidName
    String lastname;

    @NotBlank(message = "Phone number is required")
    @ValidPhone
    String phoneNumber;

    @NotBlank(message = "Address is required")
    String address;
}

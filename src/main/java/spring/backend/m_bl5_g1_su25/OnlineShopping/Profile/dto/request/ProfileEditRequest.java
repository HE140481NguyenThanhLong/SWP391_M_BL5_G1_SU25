package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileEditRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(max = 50, message = "Username không được quá 50 ký tự")
    String username;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được quá 100 ký tự")
    String email;
    @NotBlank(message = "Họ không được để trống")
    @Size(max = 50, message = "Họ không được quá 50 ký tự")
    String firstname;
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 50, message = "Tên không được quá 50 ký tự")
    String lastname;
    @Size(max = 50, message = "Số điện thoại không được quá 50 ký tự")
    String phoneNumber;
    @Size(max = 255, message = "Địa chỉ 1 không được quá 255 ký tự")
    String address1;
    @Size(max = 255, message = "Địa chỉ 2 không được quá 255 ký tự")
    String address2;
}

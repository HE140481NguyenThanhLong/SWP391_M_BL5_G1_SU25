package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReportFormResponse {
    @NotBlank
    String response;
}

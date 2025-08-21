package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReportFormRequest {
    @NotBlank(message = "Description not blank")
    String description;
    @NotBlank(message = "Title must not blank")
    String title;
    @NotBlank(message = "Issues must not empty")
    IssueType issues;
    String imgUrl;

}

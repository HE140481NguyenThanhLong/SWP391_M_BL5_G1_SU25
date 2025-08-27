package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReportFormRequest {
    MultipartFile image;
    @NotBlank(message = "Description not blank")
    String description;
    @NotBlank(message = "Title must not blank")
    String title;
    @NotNull
    IssueType issues;

//    Integer productId;

}

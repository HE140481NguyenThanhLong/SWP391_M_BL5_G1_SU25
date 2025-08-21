package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReportFormResponse {
    String title, description, imageUrl;
    IssueType issueType;
    ReportStatus status;
    LocalDateTime createdAt;
    LocalDateTime resolvedAt;
}

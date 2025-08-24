package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReportFormDefault {
    String title;
    IssueType issueType;
    String imgUrl;
    String description;
    ReportStatus status;
    LocalDateTime createdAt;
    Integer report_id;
}

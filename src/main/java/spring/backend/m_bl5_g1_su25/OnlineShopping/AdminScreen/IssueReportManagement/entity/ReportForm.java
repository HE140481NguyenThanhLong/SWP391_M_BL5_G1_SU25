package spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.auth.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="ReportIssue")
public class ReportForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer report_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    IssueType issueType = IssueType.PRODUCT_ISSUE;

    @Column( nullable = false, length = 1000)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    ReportStatus status = ReportStatus.OPENED;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    LocalDateTime resolvedAt;

}

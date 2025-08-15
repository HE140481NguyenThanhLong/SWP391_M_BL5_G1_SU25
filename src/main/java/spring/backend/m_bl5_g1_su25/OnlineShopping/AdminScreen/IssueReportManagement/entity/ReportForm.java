package spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.AdminScreen.IssueReportManagement.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_issues",
        indexes = {
                @Index(name = "idx_report_user", columnList = "user_id"),
                @Index(name = "idx_report_status", columnList = "status"),
                @Index(name = "idx_report_type", columnList = "issue_type"),
                @Index(name = "idx_report_created", columnList = "created_at")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type", nullable = false, length = 30)
    private IssueType issueType = IssueType.PRODUCT_ISSUE;

    @Column(name = "description", nullable = false, length = 1000)
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReportStatus status = ReportStatus.OPENED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}

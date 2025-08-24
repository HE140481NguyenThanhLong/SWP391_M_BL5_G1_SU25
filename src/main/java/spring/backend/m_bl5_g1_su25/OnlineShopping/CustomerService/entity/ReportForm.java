package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;

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
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(name = "staff_id")
    Staff staff;
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "product_id")
//    Product product;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    IssueType issueType = IssueType.PRODUCT_ISSUE;

    @Column( nullable = false, length = 100,columnDefinition = "NVARCHAR(100)")
    String title;
    @Column(length = 1000,columnDefinition = "VARCHAR(1000)")
    String imgUrl;


    @Column( nullable = false, length = 1000,columnDefinition = "NVARCHAR(1000)")
    String description;


    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    ReportStatus status = ReportStatus.IN_PROGRESS;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    LocalDateTime resolvedAt;

}

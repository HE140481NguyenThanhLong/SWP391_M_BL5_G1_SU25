package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReportResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer report_response_id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    ReportForm reportForm;

    @Column
    String response;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    Staff staff;
}

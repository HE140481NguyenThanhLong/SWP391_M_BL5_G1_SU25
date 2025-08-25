package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportResponse;

import java.util.Optional;

@Repository
public interface ReportFormResponseRepo extends JpaRepository<ReportResponse, Integer> {

    @Query("SELECT res FROM ReportResponse res WHERE res.reportForm.report_id = :reportId")
    Optional<ReportResponse> findByReportFormId(@Param("reportId") Integer reportId);
}

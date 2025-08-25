package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;

import java.util.List;

@Repository
public interface ReportFormRepository extends JpaRepository<ReportForm, Integer> {

    @Query("SELECT r FROM ReportForm r JOIN r.customer c JOIN c.user u WHERE u.username = :username")
    List<ReportForm> findAllByUsername(@Param("username") String username);

    @Query("SELECT r FROM ReportForm r WHERE r.report_id = :reportID")
    List<ReportForm> findAllReportFormsByReport_id(@Param("reportID") Integer reportID);
    @Query(
            value = "SELECT * FROM report_issue ORDER BY report_id DESC OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY",
            nativeQuery = true
    )
    List<ReportForm> findAllWithOffsetLimit(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT e FROM ReportForm e WHERE e.report_id= :reportID")
    ReportForm findByReportId(@Param("reportID") Integer reportID);


}


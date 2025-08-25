package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository.ReportFormRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository.ReportFormResponseRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseService    {
    ReportFormRepository reportFormRepository;
    ReportFormResponseRepo reportFormResponseRepo;

    public ResponseService(ReportFormRepository reportFormRepository, ReportFormResponseRepo reportFormResponseRepo) {
        this.reportFormRepository = reportFormRepository;
        this.reportFormResponseRepo = reportFormResponseRepo;
    }
    @Transactional
    public void createStaffResponse(Integer reportFormId, String responseText, Staff staff) {
        ReportForm originalReport = reportFormRepository.findById(reportFormId)
                .orElseThrow(()->new EntityNotFoundException("Report Form Not Found"));
        ReportResponse staffResponse = ReportResponse.builder()
                .reportForm(originalReport)
                .staff(staff)
                .response(responseText)
                .build();
        originalReport.setResolvedAt(LocalDateTime.now());
        originalReport.setStatus(ReportStatus.RESOLVED);
        reportFormRepository.save(originalReport);
        reportFormResponseRepo.save(staffResponse);
    }
}

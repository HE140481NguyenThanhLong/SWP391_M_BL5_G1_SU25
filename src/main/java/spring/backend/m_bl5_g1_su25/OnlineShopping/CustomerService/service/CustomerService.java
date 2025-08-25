package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.service;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.request.ReportFormRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormDefault;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponseForStaff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponseForCustomer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.mapper.CustomerServiceMapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository.ReportFormRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository.ReportFormResponseRepo;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.StaffRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CustomerService {

    ReportFormRepository reportRepo;
    StaffRepository staffRepo;
    ProductRepository productRepo;
    CustomerServiceMapper customerServiceMapper;
    ReportFormResponseRepo reportFormResponseRepo;



    @Autowired
    public CustomerService(ReportFormRepository reportRepo, StaffRepository staffRepo, ProductRepository productRepo, CustomerServiceMapper customerServiceMapper, ReportFormResponseRepo reportFormResponseRepo) {
        this.reportRepo = reportRepo;
        this.staffRepo = staffRepo;
        this.productRepo = productRepo;
        this.customerServiceMapper = customerServiceMapper;
        this.reportFormResponseRepo = reportFormResponseRepo;
    }



    @Transactional
    public void createReportFromCustomer(ReportFormRequest request, Customer customer) {
        ReportForm newReport = new ReportForm();
        newReport.setTitle(request.getTitle());
        newReport.setIssueType(request.getIssues());
        newReport.setDescription(request.getDescription());
        newReport.setImgUrl(request.getImgUrl());
        newReport.setCustomer(customer);
        newReport.setStatus(ReportStatus.IN_PROGRESS);
        newReport.setCreatedAt(LocalDateTime.now());

        Staff assignedStaff = findAvailableStaff();
        newReport.setStaff(assignedStaff);


        newReport.setStatus(ReportStatus.IN_PROGRESS);


            reportRepo.save(newReport);
    }


    private Staff findAvailableStaff() {
        List<Staff> availableStaff = staffRepo.findAll();
        if (availableStaff.isEmpty()) {
            return null;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(availableStaff.size());
        return availableStaff.get(randomIndex);
    }
    public List<ReportFormResponseForCustomer> findReportByCustomer(String customerName) {
        List<ReportForm> reportForms = reportRepo.findAllByUsername(customerName);

        return reportForms.stream().map(reportForm -> {
            Optional< ReportResponse> response =reportFormResponseRepo.findByReportFormId(reportForm.getReport_id());

            return ReportFormResponseForCustomer.builder()
                    .title(reportForm.getTitle())
                    .status(reportForm.getStatus())
                    .createdAt(reportForm.getCreatedAt())
                    .description(reportForm.getDescription())
                    .imgUrl(reportForm.getImgUrl())
                    .resolveAt(reportForm.getResolvedAt())
                    .staffResponse(response.map(ReportResponse::getResponse).orElse(null))
                    .build();
        }).collect(Collectors.toList());
    }
    public List<ReportFormResponseForStaff> findAllByReportId(Integer reportID) {
        return reportRepo.findAllReportFormsByReport_id(reportID)
                .stream()
                .map(customerServiceMapper::toReportForm)
                .collect(Collectors.toList());
    }

    public ReportForm findReportByReportId(Integer reportID) {
        return  reportRepo.findByReportId(reportID);
    }
    public List<ReportFormDefault> findAllReports(int page,int size){
        int offset = page*size;
        return reportRepo.findAllWithOffsetLimit(page,offset)
                .stream()
                .map(customerServiceMapper::toReportFormDefault)
                .collect(Collectors.toList());
    }
}

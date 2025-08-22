package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.service;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.request.ReportFormRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.repository.ReportFormRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.ProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.StaffRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CustomerService {

    ReportFormRepository reportRepo;
    StaffRepository staffRepo;
    ProductRepository productRepo;
    @Autowired
    public CustomerService(ReportFormRepository reportRepo, StaffRepository staffRepo, ProductRepository productRepo) {
        this.reportRepo = reportRepo;
        this.staffRepo = staffRepo;
        this.productRepo = productRepo;
    }
    @Transactional
    public void createReportFromCustomer(ReportFormRequest request, Customer customer) {
        ReportForm newReport = new ReportForm();

        // 1. Map all data from the DTO to the ReportForm entity
        newReport.setTitle(request.getTitle());
        newReport.setIssueType(request.getIssues());
        newReport.setDescription(request.getDescription());
        newReport.setImgUrl(request.getImgUrl());

        // 2. Set the customer who submitted the report
        newReport.setCustomer(customer);
        newReport.setStatus(ReportStatus.IN_PROGRESS);
        newReport.setCreatedAt(LocalDateTime.now());

        // 3. If a productId was submitted, find and link the product
        if (request.getProductId() != null) {
            Product product = productRepo.findById(request.getProductId())
                    .orElse(null); // Find the product, or return null if not found
            newReport.setProduct(product);
        }

        // 4. Find an available staff member to assign the report to
        Staff assignedStaff = findAvailableStaff();
        newReport.setStaff(assignedStaff); // This can be null if no staff is available

        // 5. Set the initial status of the report
        newReport.setStatus(ReportStatus.IN_PROGRESS); // Or "NEW", "OPEN", etc.

        // 6. Save the completed entity to the database
            reportRepo.save(newReport);
    }

    /**
     * Logic to find a staff member for assignment.
     */
    private Staff findAvailableStaff() {
        List<Staff> availableStaff = staffRepo.findAll(); // Or find only active staff
        if (availableStaff.isEmpty()) {
            return null; // Important: Handle this case gracefully
        }
        // Use a simple random assignment for now
        int randomIndex = ThreadLocalRandom.current().nextInt(availableStaff.size());
        return availableStaff.get(randomIndex);
    }
}

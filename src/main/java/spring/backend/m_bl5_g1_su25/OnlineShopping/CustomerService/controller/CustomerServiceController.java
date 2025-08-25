package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.request.ReportFormRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormDefault;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponseForStaff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponseForCustomer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.service.CustomerService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.service.ResponseService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.CustomerRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.repository.StaffRepository;

import java.util.List;

@Controller
@RequestMapping("/customerService")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerServiceController {
    CustomerRepository customerRepository;
     CustomerService reportService;
     ProductService productService;
    private final ModelMapper modelMapper;
    ResponseService responseService;
    StaffRepository staffRepository;


    @Autowired
    public CustomerServiceController(ResponseService responseService, ModelMapper modelMapper, ProductService productService, CustomerService reportService,
                                     CustomerRepository customerRepository,StaffRepository staffRepository) {
        this.responseService = responseService;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.reportService = reportService;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }



    @GetMapping("/reportHistoryForCustomer")
    public String showFormHistory(Model model, HttpSession session,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String userName = loggedInUser.getUsername();
       // String customerName=customerRepository.findCustomerNameByUsername(userName);
        List<ReportFormResponseForCustomer> reportForm = reportService.findReportByCustomer(userName);

        model.addAttribute("reportForm", reportForm);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "/customerService/reportHistoryForCustomer";

    }

    @GetMapping("/reportForm")
    public String showNewReportForm(Model model) {
        model.addAttribute("ReportFormRequest", new ReportFormRequest());
        model.addAttribute("issueTypes", IssueType.values());

        return "/customerService/reportForm";
    }


    @PostMapping("/reportForm")
    public String handleSubmitReport(@Valid @ModelAttribute("ReportFormRequest") ReportFormRequest ReportFormRequest,
                                     BindingResult bindingResult,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {


        if (bindingResult.hasErrors()) {
            System.out.println("VALIDATION FAILED: " + bindingResult.getAllErrors());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("issues", IssueType.values());
            // "/customerService/reportHistoryForCustomer"
            return "/customerService/reportForm";
        }


        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getRole() != Role.CUSTOMER) {
            return "redirect:/authority/signin";
        }
        String  username = loggedInUser.getUsername();

        Customer customer = customerRepository.findCustomerByUsername(username);

        reportService.createReportFromCustomer(ReportFormRequest,customer);


        redirectAttributes.addFlashAttribute("successMessage", "Your report has been submitted successfully!");
        return "redirect:/guest";
    }
    @GetMapping("/reportViewForStaff")
    public String showReportReviewPage(Model model,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        List<ReportFormDefault> allReports = reportService.findAllReports(page,size);
        if (size <= 0) {
            size = 10;
        }
        boolean hasNextPage = allReports.size() == size;
        model.addAttribute("allReports", allReports);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasNextPage", hasNextPage);
        return "/customerService/reportViewForStaff";
    }


    @GetMapping("/{reportId}/respondFormForStaff")
    public String showRespondPage(@PathVariable Integer reportId, Model model) {
        ReportForm respondreport = reportService.findReportByReportId(reportId);
        model.addAttribute("respondreport", respondreport);
        return "/customerService/respondFormForStaff";
    }


    @PostMapping("/{reportId}/respond")
    public String submitResponse(@PathVariable Integer reportId,
                                 @RequestParam("responseText") String responseText,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {


        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Staff staff = staffRepository.findStaffByUserId(loggedInUser.getUser_id());
        responseService.createStaffResponse(reportId, responseText, staff);

        redirectAttributes.addFlashAttribute("successMessage", "Response sent successfully!");
        return "redirect:/customerService/reportViewForStaff";
    }

}

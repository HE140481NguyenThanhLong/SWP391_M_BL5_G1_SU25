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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.request.ReportFormRequest;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.IssueType;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.service.CustomerService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.reponse.UserResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.enums.Role;

import java.util.List;

@Controller
@RequestMapping("/customerService")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerServiceController {
     CustomerService reportService;
     ProductService productService; // Service to fetch products
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerServiceController(CustomerService reportService, ProductService productService,
                                     ModelMapper modelMapper) {
        this.reportService = reportService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    /**
     * Displays the new report form to the customer.
     * It pre-populates the form with necessary data like products and issue types.
     */
    @GetMapping("/reportForm")
    public String showNewReportForm(Model model) {
        // 1. Add an empty request object for the form to bind its fields to
        model.addAttribute("ReportFormRequest", new ReportFormRequest());

        // 2. Add the list of all available products for the dropdown
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        // 3. Add the list of all possible issue types for the dropdown
        model.addAttribute("issueTypes", IssueType.values());

        return "/customerService/reportForm"; // The name of your HTML template file
    }

    /**
     * Handles the submission of the new report form.
     */
    @PostMapping("/reportForm")
    public String handleSubmitReport(@Valid @ModelAttribute("reportRequest") ReportFormRequest reportRequest,
                                     BindingResult bindingResult,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        // Check for validation errors defined in the DTO
        if (bindingResult.hasErrors()) {
            // If errors exist, repopulate the form data and return to the form to show the errors
            model.addAttribute("products", productService.findAll());
            model.addAttribute("issues", IssueType.values());
            return "/customerService/reportForm";
        }

        // Get the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getRole() != Role.CUSTOMER) {
            return "redirect:/authority/sigin"; // User must be a logged-in customer
        }
        Customer customer = modelMapper.map(loggedInUser, Customer.class);
        // Call the service to perform the business logic
        reportService.createReportFromCustomer(reportRequest,customer);

        // Add a success message and redirect
        redirectAttributes.addFlashAttribute("successMessage", "Your report has been submitted successfully!");
        return "redirect:/customerService/reportHistoryForCustomer"; // Redirect to the customer's report history page
    }
}

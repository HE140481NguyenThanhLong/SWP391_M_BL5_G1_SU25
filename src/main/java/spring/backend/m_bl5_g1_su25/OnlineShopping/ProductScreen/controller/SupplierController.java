package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Supplier;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.SupplierService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/staff/suppliers")
    public String listSuppliers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {
        int pageSize = 5;
        Page<Supplier> supplierPage = supplierService.getSuppliers(search, productType, status, region, page, pageSize, sortField, sortDir);

        List<Integer> pageNumbers = new ArrayList<>();
        int totalPages = supplierPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 2);
            int end = Math.min(totalPages, page + 2);
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }
        }

        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("search", search);
        model.addAttribute("productType", productType);
        model.addAttribute("status", status);
        model.addAttribute("region", region);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("stats", supplierService.getStats());

        return "product/supplier_manage";
    }

    @GetMapping("/staff/suppliers/add")
    public String addSupplierForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("action", "add");
        return "product/supplier-form";
    }

    @GetMapping("/staff/suppliers/edit/{id}")
    public String editSupplierForm(@PathVariable Integer id, Model model) {
        Supplier supplier = supplierService.getSupplierById(id);
        model.addAttribute("supplier", supplier);
        model.addAttribute("action", "edit");
        return "product/supplier-form";
    }

    @PostMapping("/staff/suppliers/save")
    public String saveSupplier(@Valid @ModelAttribute Supplier supplier, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", supplier.getSupplier_id() == null ? "add" : "edit");
            return "product/supplier-form";
        }
        supplierService.saveSupplier(supplier);
        return "redirect:/staff/suppliers";
    }

    @DeleteMapping("/staff/suppliers/{id}")
    @ResponseBody
    public String deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return "Supplier deleted successfully";
    }

    @GetMapping("/staff/suppliers/detail/{id}")
    public String detailSupplier(@PathVariable Integer id, Model model) {
        Supplier supplier = supplierService.getSupplierById(id);
        model.addAttribute("supplier", supplier);
        return "product/supplier-detail-template";
    }
}

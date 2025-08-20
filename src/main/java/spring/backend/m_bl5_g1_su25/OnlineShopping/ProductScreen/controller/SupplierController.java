package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Supplier;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.SupplierRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.SupplierService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String listSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "supplier/list"; // supplier/list.html
    }

    @GetMapping("/add")
    public String addSupplierForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "supplier/add";
    }

    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return "redirect:/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String editSupplier(@PathVariable Integer id, Model model) {
        model.addAttribute("supplier", supplierService.getSupplierById(id));
        return "supplier/edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return "redirect:/suppliers";
    }

}



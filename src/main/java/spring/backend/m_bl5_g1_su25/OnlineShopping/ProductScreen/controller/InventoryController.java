package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.InventoryService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/staff/inventory")
    public String listInventory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String stockStatus,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        int pageSize = 5;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        Page<Product> productPage = inventoryService.getInventory(search, category, stockStatus, page, pageSize, sortField, sortDir);
        List<Product> formattedProducts = productPage.getContent().stream().map(product -> {
            Product p = new Product();
            p.setProduct_id(product.getProduct_id());
            p.setName(product.getName());
            p.setSku(product.getSku());
            p.setCategories(product.getCategories());
            p.setQuantity(product.getQuantity());
            p.setMinQuantity(product.getMinQuantity());
            p.setImportPrice(product.getImportPrice());
            p.setSalePrice(product.getSalePrice());
            p.setBrand(product.getBrand());
            p.setImageUrl(product.getImageUrl());
            // Add formatted price fields
            try {
                p.setAdditionalProperty("formattedImportPrice", currencyFormat.format(product.getImportPrice()));
                p.setAdditionalProperty("formattedSalePrice", currencyFormat.format(product.getSalePrice()));
            } catch (Exception e) {
                p.setAdditionalProperty("formattedImportPrice", product.getImportPrice() + " ₫");
                p.setAdditionalProperty("formattedSalePrice", product.getSalePrice() + " ₫");
            }
            return p;
        }).toList();

        List<Integer> pageNumbers = new ArrayList<>();
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 2);
            int end = Math.min(totalPages, page + 2);
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }
        }

        model.addAttribute("products", formattedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("stockStatus", stockStatus);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("stats", inventoryService.getStats());
        model.addAttribute("categories", inventoryService.getAllCategories());

        return "product/inventory_manage";
    }

    @GetMapping("/staff/inventory/import")
    public String importInventoryForm(Model model) {
        model.addAttribute("products", inventoryService.getAllProducts());
        return "product/import_inventory";
    }

    @PostMapping("/staff/inventory/import")
    public String importInventory(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            Model model
    ) {
        try {
            if (quantity <= 0) {
                model.addAttribute("error", "Số lượng nhập phải lớn hơn 0");
                model.addAttribute("products", inventoryService.getAllProducts());
                return "product/import_inventory";
            }
            inventoryService.importProduct(productId, quantity);
            return "redirect:/staff/inventory";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi nhập hàng: " + e.getMessage());
            model.addAttribute("products", inventoryService.getAllProducts());
            return "product/import_inventory";
        }
    }

    @GetMapping("/staff/inventory/export")
    public ResponseEntity<List<Product>> exportInventory() {
        List<Product> products = inventoryService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}

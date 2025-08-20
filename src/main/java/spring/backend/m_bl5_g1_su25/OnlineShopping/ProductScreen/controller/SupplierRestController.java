package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Supplier;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.SupplierRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierRestController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public List<Map<String, Object>> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(s -> {
                    Map<String, Object> supplier = new HashMap<>();
                    supplier.put("supplier_id", s.getSupplier_id());
                    supplier.put("name", s.getName());
                    return supplier;
                })
                .toList();
    }

    @GetMapping("/{supplierId}/products")
    public ResponseEntity<List<Product>> getProductsBySupplier(
            @PathVariable Integer supplierId,
            @RequestParam(name = "category_id", required = false) Integer categoryId
    ) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        List<Product> products = new ArrayList<>(supplier.getProducts());
        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getCategories().stream().anyMatch(c -> c.getCategory_id().equals(categoryId)))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }
}
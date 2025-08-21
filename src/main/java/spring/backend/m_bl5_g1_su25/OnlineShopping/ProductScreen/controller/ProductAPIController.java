package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.SupplierService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductAPIController {

    @Autowired
    private ProductService productService;




    @GetMapping("/api/suppliers/{supplierId}/products")
    public List<Product> getProductsBySupplier(
            @PathVariable Integer supplierId,
            @RequestParam(name = "category_id", required = false) Integer categoryId
    ) {
        if (supplierId == null) {
            throw new IllegalArgumentException("Supplier ID is required");
        }
        Page<Product> products = productService.filterProducts(
                null, null, supplierId.toString(), categoryId, "createdAt", 0, 12
        );
        return products.getContent();
    }
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> dto = new HashMap<>();
        dto.put("id", product.getProduct_id());
        dto.put("name", product.getName());
        dto.put("price", product.getPrice());
        dto.put("importPrice", product.getImportPrice());
        return ResponseEntity.ok(dto);
    }

}
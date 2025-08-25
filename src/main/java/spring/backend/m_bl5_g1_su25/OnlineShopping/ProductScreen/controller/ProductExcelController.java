package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service.ProductExcelService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductExcelController {

    private final ProductExcelService productExcelService;

    @GetMapping("/export-template")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        String filename = URLEncoder.encode("product_template.xlsx", StandardCharsets.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        productExcelService.exportTemplate(response.getOutputStream());
    }

    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws Exception {
        String filename = URLEncoder.encode("products.xlsx", StandardCharsets.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        productExcelService.exportData(response.getOutputStream());
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(@RequestPart("file") MultipartFile file) throws Exception {
        List<String> errors = productExcelService.importExcel(file);
        if (errors.isEmpty()) {
            return ResponseEntity.ok("Import thành công");
        } else {
            return ResponseEntity.badRequest().body(errors);
        }
    }
}

package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.Status;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.*;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductExcelService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;

    // =========== EXPORT TEMPLATE ===========
    public void exportTemplate(OutputStream os) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("SKU*");
            header.createCell(1).setCellValue("Name*");
            header.createCell(2).setCellValue("Description*");
            header.createCell(3).setCellValue("Price*");
            header.createCell(4).setCellValue("Import Price*");
            header.createCell(5).setCellValue("Sale Price*");
            header.createCell(6).setCellValue("Quantity");
            header.createCell(7).setCellValue("Min Quantity*");
            header.createCell(8).setCellValue("Status* (IN_STOCK/OUT_OF_STOCK)");
            header.createCell(9).setCellValue("SupplierId");
            header.createCell(10).setCellValue("Categories (comma separated)");
            header.createCell(11).setCellValue("Brand");
            header.createCell(12).setCellValue("Discount");

            for (int i = 0; i <= 12; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(os);
        }
    }

    // =========== EXPORT DỮ LIỆU ===========
    public void exportData(OutputStream os) throws Exception {
        List<Product> products = productRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row header = sheet.createRow(0);
            String[] columns = {"SKU","Name","Description","Price","ImportPrice","SalePrice","Quantity",
                    "MinQuantity","Status","SupplierId","Categories","Brand","Discount"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowNum = 1;
            for (Product p : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getSku());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getDescription());
                row.createCell(3).setCellValue(p.getPrice().doubleValue());
                row.createCell(4).setCellValue(p.getImportPrice().doubleValue());
                row.createCell(5).setCellValue(p.getSalePrice().doubleValue());
                row.createCell(6).setCellValue(p.getQuantity() == null ? 0 : p.getQuantity());
                row.createCell(7).setCellValue(p.getMinQuantity());
                row.createCell(8).setCellValue(p.getStatus().name());
                row.createCell(9).setCellValue(p.getSupplier() != null ? p.getSupplier().getSupplier_id() : 0);
                row.createCell(10).setCellValue(String.join(",",
                        p.getCategories().stream().map(Category::getName).toList()));
                row.createCell(11).setCellValue(p.getBrand());
                row.createCell(12).setCellValue(p.getDiscount());
            }

            for (int i = 0; i <= 12; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(os);
        }
    }

    // =========== IMPORT ===========
    public List<String> importExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String sku = getString(row.getCell(0));
                    String name = getString(row.getCell(1));
                    String description = getString(row.getCell(2));
                    BigDecimal price = getBigDecimal(row.getCell(3));
                    BigDecimal importPrice = getBigDecimal(row.getCell(4));
                    BigDecimal salePrice = getBigDecimal(row.getCell(5));
                    Integer quantity = getInteger(row.getCell(6));
                    Integer minQuantity = getInteger(row.getCell(7));
                    String statusStr = getString(row.getCell(8));
                    Integer supplierId = getInteger(row.getCell(9));
                    String categoriesStr = getString(row.getCell(10));
                    String brand = getString(row.getCell(11));
                    Integer discount = getInteger(row.getCell(12));

                    if (sku == null || name == null || price == null || importPrice == null || salePrice == null || minQuantity == null || statusStr == null) {
                        errors.add("Row " + (i+1) + " thiếu dữ liệu bắt buộc.");
                        continue;
                    }

                    Status status;
                    try {
                        status = Status.valueOf(statusStr);
                    } catch (Exception ex) {
                        errors.add("Row " + (i+1) + " status không hợp lệ.");
                        continue;
                    }

                    Supplier supplier = null;
                    if (supplierId != null) {
                        supplier = supplierRepository.findById(supplierId).orElse(null);
                    }

                    Set<Category> categories = new HashSet<>();
                    if (categoriesStr != null && !categoriesStr.isBlank()) {
                        String[] names = categoriesStr.split(",");
                        for (String catName : names) {
                            categoryRepository.findByName(catName.trim()).ifPresent(categories::add);
                        }
                    }

                    Product product = productRepository.findBySku(sku)
                            .orElse(new Product());
                    product.setSku(sku);
                    product.setName(name);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setImportPrice(importPrice);
                    product.setSalePrice(salePrice);
                    product.setQuantity(quantity);
                    product.setMinQuantity(minQuantity);
                    product.setStatus(status);
                    product.setSupplier(supplier);
                    product.setCategories(categories);
                    product.setBrand(brand);
                    product.setDiscount(discount != null ? discount : 0);

                    productRepository.save(product);

                } catch (Exception e) {
                    errors.add("Row " + (i+1) + " lỗi: " + e.getMessage());
                }
            }
        }
        return errors;
    }

    // ===== Helpers =====
    private String getString(Cell cell) {
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING ?
                cell.getStringCellValue().trim() :
                String.valueOf((int)cell.getNumericCellValue());
    }

    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return new BigDecimal(cell.getStringCellValue().trim());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private Integer getInteger(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

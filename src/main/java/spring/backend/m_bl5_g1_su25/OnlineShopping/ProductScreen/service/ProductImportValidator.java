package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import java.math.BigDecimal;

public class ProductImportValidator {

    public static String validate(String sku, String name, BigDecimal price, Integer quantity, Boolean active) {
        if (sku == null || sku.isBlank()) return "SKU bắt buộc.";
        if (name == null || name.isBlank()) return "Tên sản phẩm bắt buộc.";
        if (price == null) return "Giá bắt buộc.";
        if (price.signum() < 0) return "Giá phải >= 0.";
        if (quantity == null) return "Số lượng bắt buộc.";
        if (quantity < 0) return "Số lượng phải >= 0.";
        if (active == null) return "Trạng thái (Active) bắt buộc (TRUE/FALSE).";
        return null; // ok
    }
}

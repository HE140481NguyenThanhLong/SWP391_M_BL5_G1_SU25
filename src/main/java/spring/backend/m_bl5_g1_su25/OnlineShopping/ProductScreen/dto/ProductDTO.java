package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto;



import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Category;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {
    private Integer product_id;
    private String name;
    private BigDecimal price;
    private BigDecimal importPrice;
    private List<String> categories;

    public ProductDTO(Product product) {
        this.product_id = product.getProduct_id();
        this.name = product.getName();
        this.price = product.getPrice();
        this.importPrice = product.getImportPrice();
        this.categories = product.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    // Getter + Setter (nếu cần)
    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
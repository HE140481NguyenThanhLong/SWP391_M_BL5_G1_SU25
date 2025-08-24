package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.request;

public class ProductImportRequest {
    private Integer product_id;
    private Integer quantity;

    // getter setter
    public Integer getProduct_id() {
        return product_id;
    }
    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
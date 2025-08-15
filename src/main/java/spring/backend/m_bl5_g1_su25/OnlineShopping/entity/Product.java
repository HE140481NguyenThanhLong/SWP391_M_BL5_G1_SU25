package spring.backend.m_bl5_g1_su25.OnlineShopping.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    @Column(name = "product_name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Column(name = "product_code", nullable = false, unique = true, length = 50)
    private String code;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "import_date")
    private LocalDate importDate;

    @Size(max = 100, message = "Supplier must not exceed 100 characters")
    @Column(name = "supplier", length = 100)
    private String supplier;

    @Positive(message = "Import price must be positive")
    @Column(name = "price_import")
    private Double priceImport;

    @Positive(message = "Sell price must be positive")
    @Column(name = "price_sell")
    private Double priceSell;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 50, message = "Version must not exceed 50 characters")
    @Column(name = "version", length = 50)
    private String version;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    @Column(name = "color", length = 50)
    private String color;

    @Size(max = 50, message = "Size must not exceed 50 characters")
    @Column(name = "size", length = 50)
    private String size;

    @Min(value = 1900, message = "Year of manufacture must be after 1900")
    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;
}

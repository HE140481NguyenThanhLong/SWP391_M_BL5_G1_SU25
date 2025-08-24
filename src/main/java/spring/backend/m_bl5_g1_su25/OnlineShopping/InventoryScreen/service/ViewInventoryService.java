package spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.dto.InventoryStatisticsDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.dto.InventoryProductDto;
import spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.repository.InventoryProductRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ViewInventoryService {

    InventoryProductRepository inventoryProductRepository;

    public InventoryStatisticsDto getInventoryStatistics(String status, String keyword) {
        log.info("=== INVENTORY SERVICE CALLED ===");
        log.info("Parameters: status={}, keyword={}", status, keyword);

        List<Product> products = getFilteredProducts(status, keyword);
        log.info("Retrieved {} products", products.size());

        Long totalProducts = inventoryProductRepository.count();
        Long lowStock = inventoryProductRepository.countLowStockProducts();
        Long outOfStock = inventoryProductRepository.countOutOfStockProducts();

        log.info("Statistics: total={}, lowStock={}, outOfStock={}", totalProducts, lowStock, outOfStock);

        return InventoryStatisticsDto.builder()
                .totalProducts(totalProducts)
                .lowStockProducts(lowStock)
                .outOfStockProducts(outOfStock)
                .inventoryProducts(convertToDto(products))
                .build();
    }

    private List<Product> getFilteredProducts(String status, String keyword) {
        log.info("Getting filtered products: status={}, keyword={}", status, keyword);

        boolean hasNoFilter = (status == null || "all".equals(status)) &&
                             (keyword == null || keyword.trim().isEmpty());

        if (hasNoFilter) {
            log.info("No filters applied, getting all products");
            return inventoryProductRepository.findAllInventoryProductsList();
        } else {
            log.info("Applying filters");
            String safeStatus = status != null ? status : "all";
            String safeKeyword = keyword != null ? keyword.trim() : "";
            return inventoryProductRepository.findByStatusAndKeywordList(safeStatus, safeKeyword);
        }
    }

    private List<InventoryProductDto> convertToDto(List<Product> products) {
        log.info("Converting {} products to DTOs", products.size());

        return products.stream()
                .map(product -> InventoryProductDto.builder()
                        .productId(product.getProduct_id())
                        .name(product.getName())
                        .description(product.getDescription())
                        .quantity(product.getQuantity() != null ? product.getQuantity() : 0)
                        .price(product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO)
                        .importPrice(product.getImportPrice() != null ? product.getImportPrice() : BigDecimal.ZERO)
                        .salePrice(product.getSalePrice() != null ? product.getSalePrice() : BigDecimal.ZERO)
                        .soldCount(product.getSoldCount())
                        .imageUrl(product.getImageUrl() != null ? product.getImageUrl() : "")
                        .build())
                .collect(Collectors.toList());
    }
}

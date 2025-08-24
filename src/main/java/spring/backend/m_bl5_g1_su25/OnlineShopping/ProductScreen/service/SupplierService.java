package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.service;

import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Supplier;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.SupplierStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository.SupplierRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

    @Autowired
    private SupplierRepository supplierRepository;

    public Page<Supplier> getSuppliers(String search, String productType, String status, String region, int page, int size, String sortField, String sortDir) {
        logger.info("Fetching suppliers with search: {}, productType: {}, status: {}, region: {}, page: {}, size: {}, sortField: {}, sortDir: {}",
                search, productType, status, region, page, size, sortField, sortDir);

        Specification<Supplier> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (search != null && !search.trim().isEmpty()) {
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%")
                ));
            }
            if (productType != null && !productType.trim().isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("productType"), productType));
            }
            if (status != null && !status.trim().isEmpty()) {
                try {
                    SupplierStatus supplierStatus = SupplierStatus.valueOf(status.toUpperCase());
                    predicate = cb.and(predicate, cb.equal(root.get("status"), supplierStatus));
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid status value: {}", status);
                }
            }
            if (region != null && !region.trim().isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("address")), "%" + region.toLowerCase() + "%"));
            }
            return predicate;
        };

        Sort sort = Sort.by(sortField);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Supplier> result = supplierRepository.findAll(spec, pageable);
        logger.info("Retrieved {} suppliers", result.getTotalElements());
        return result;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSuppliers", supplierRepository.count());
        stats.put("activeSuppliers", supplierRepository.countByStatus(SupplierStatus.ACTIVE));
        stats.put("inactiveSuppliers", supplierRepository.countByStatus(SupplierStatus.INACTIVE));
        stats.put("monthlyOrders", 156); // Placeholder: Replace with actual order query
        return stats;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier saveSupplier(Supplier supplier) {
        logger.info("Saving supplier: {}", supplier.getName());
        return supplierRepository.save(supplier);
    }

    public Supplier getSupplierById(Integer id) {
        logger.info("Fetching supplier with ID: {}", id);
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp ID: " + id));
    }

    public void deleteSupplier(Integer id) {
        logger.info("Deleting supplier with ID: {}", id);
        supplierRepository.deleteById(id);
    }
}

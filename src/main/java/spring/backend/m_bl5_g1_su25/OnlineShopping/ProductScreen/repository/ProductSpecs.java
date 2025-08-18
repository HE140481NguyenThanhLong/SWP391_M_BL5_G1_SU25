package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.repository;

import org.springframework.data.jpa.domain.Specification;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.enums.Status;

import java.math.BigDecimal;
import java.util.List;

public class ProductSpecs {
    private ProductSpecs() {}

    public static Specification<Product> statusIn(List<Status> statuses) {
        return (root, q, cb) -> {
            if (statuses == null || statuses.isEmpty()) return cb.conjunction();
            return root.get("status").in(statuses);
        };
    }

    public static Specification<Product> categoryIn(List<Integer> categoryIds) {
        return (root, q, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) return cb.conjunction();
            // products <-> categories is ManyToMany
            var join = root.join("categories"); // Set<Category> categories
            q.distinct(true);                   // avoid duplicates
            return join.get("category_id").in(categoryIds);
        };
    }

    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, q, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null)  return cb.between(root.get("price"), min, max);
            if (min != null)                 return cb.greaterThanOrEqualTo(root.get("price"), min);
            return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    public static Specification<Product> nameContains(String qtext) {
        return (root, q, cb) -> {
            if (qtext == null || qtext.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + qtext.toLowerCase() + "%");
        };
    }
}

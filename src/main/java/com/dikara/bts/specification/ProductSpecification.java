package com.dikara.bts.specification;

import com.dikara.bts.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> search(
            String keyword,
            String category
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + keyword.toLowerCase() + "%"
                        )
                );
            }

            if (StringUtils.hasText(category)) {
                predicates.add(
                        cb.equal(
                                root.get("category"),
                                category
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}

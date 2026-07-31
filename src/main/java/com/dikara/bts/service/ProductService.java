package com.dikara.bts.service;

import com.dikara.bts.dto.request.ProductRequest;
import com.dikara.bts.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductResponse> getProducts(
            String search,
            String category,
            int page,
            int limit
    );

    ProductResponse getById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(
            Long id,
            ProductRequest request
    );

    void delete(Long id);
}

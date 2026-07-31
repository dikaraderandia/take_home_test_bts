package com.dikara.bts.service.impl;

import com.dikara.bts.dto.request.ProductRequest;
import com.dikara.bts.dto.response.ProductResponse;
import com.dikara.bts.entity.Product;
import com.dikara.bts.entity.User;
import com.dikara.bts.exception.ResourceNotFoundException;
import com.dikara.bts.repository.ProductRepository;
import com.dikara.bts.repository.UserRepository;
import com.dikara.bts.service.ProductService;
import com.dikara.bts.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailServiceImpl userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(
            String search,
            String category,
            int page,
            int limit
    ) {

        Pageable pageable =
                PageRequest.of(page, limit);

        Specification<Product> specification =
                ProductSpecification.search(
                        search,
                        category
                );

        Page<ProductResponse> res = productRepository
                .findAll(specification, pageable)
                .map(this::toResponse);

        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                ));

        return toResponse(product);
    }

    @Override
    public ProductResponse create(
            ProductRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();


        assert authentication != null;
        String email = authentication.getName();

        User user = userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Product product = new Product();

        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setImages(request.getImages());

        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy(user.getUsername());
        product.setCreatedById(String.valueOf(user.getId()));

        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy(user.getUsername());
        product.setUpdatedById(String.valueOf(user.getId()));

        Product saved =
                productRepository.save(product);

        return toResponse(saved);
    }

    @Override
    public ProductResponse update(
            Long id,
            ProductRequest request
    ) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                ));

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        assert authentication != null;
        String email = authentication.getName();

        User user = userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setImages(request.getImages());

        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy(user.getUsername());
        product.setUpdatedById(String.valueOf(user.getId()));

        Product updated =
                productRepository.save(product);

        return toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"
                                ));

        productRepository.delete(product);
    }

    private ProductResponse toResponse(
            Product product
    ) {

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory())
                .images(product.getImages())
                .createdAt(product.getCreatedAt())
                .createdBy(product.getCreatedBy())
                .createdById(product.getCreatedById())
                .updatedAt(product.getUpdatedAt())
                .updatedBy(product.getUpdatedBy())
                .updatedById(product.getUpdatedById())
                .build();
    }
}
package com.nhatquang99.api.service;

import com.nhatquang99.api.payload.request.ProductRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductService {
    ListResponse findAll(Pageable pageable);

    ListResponse findByCategoryOrName(String category, String name, Pageable pageable);

    GenericResponse create(ProductRequest productRequest);

    GenericResponse update(UUID id, ProductRequest productRequest);

    GenericResponse delete(UUID id);

    Object getTotalProduct();

    GenericResponse getProductById(UUID id);
}

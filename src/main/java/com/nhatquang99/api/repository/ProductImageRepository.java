package com.nhatquang99.api.repository;

import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    List<ProductImage> findAllByProduct(Product product);
}

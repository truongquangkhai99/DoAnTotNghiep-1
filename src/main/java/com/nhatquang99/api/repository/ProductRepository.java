package com.nhatquang99.api.repository;

import com.nhatquang99.api.model.Category;
import com.nhatquang99.api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByCategory(Category category);
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByNameIgnoreCaseContaining(String name, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}

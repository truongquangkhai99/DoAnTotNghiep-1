package com.nhatquang99.api.repository;

import com.nhatquang99.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByParentIdIsNull();
    List<Category> findByParentId(UUID uuid);
    boolean existsByCode(String code);
    Category findByCode(String code);
}

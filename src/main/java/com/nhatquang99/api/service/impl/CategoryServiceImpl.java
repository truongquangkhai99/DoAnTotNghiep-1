package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.mapper.CategoryMapper;
import com.nhatquang99.api.model.Category;
import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.payload.request.CategoryRequest;
import com.nhatquang99.api.payload.response.CategoryResponse;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;
import com.nhatquang99.api.repository.CategoryRepository;
import com.nhatquang99.api.repository.ProductRepository;
import com.nhatquang99.api.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Override
    public ListResponse findAll() {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        List<Category> categoryParents = categoryRepository.findByParentIdIsNull();

        // Chạy vòng lặp parent
        for (Category categoryParent : categoryParents) {
            CategoryResponse categoryRes = categoryMapper.toCategoryRes(categoryParent);

            List<CategoryResponse> typeList = new ArrayList<>();
            // Chạy vòng lặp child
            for (Category categoryChild : categoryRepository.findByParentId(categoryParent.getId())) {
                CategoryResponse categoryResChild = categoryMapper.toCategoryRes(categoryChild);
                typeList.add(categoryResChild);
            }

            categoryRes.setTypeList(typeList);
            categoryResponses.add(categoryRes);
        }

        ListResponse response = new ListResponse();
        response.setNumberOfEntities(categoryRepository.count());
        response.setSizeList(categoryResponses.size());
        response.setList(categoryResponses);

        return response;
    }

    @Override
    public GenericResponse create(CategoryRequest categoryRequest) {
        // Kiểm tra xem mã code danh mục có bị trùng
        String categoryCode = categoryRequest.getTypeCode();
        if (categoryRepository.existsByCode(categoryCode)) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Tạo thất bại! Đã tồn tại category này.", null);
        }
        // Kiểm tra nếu thêm danh mục con
        if (categoryRequest.getCategoryParentID() != null) {
            // Kiểm tra xem danh mục cha có tồn tại
            if (!categoryRepository.existsById(categoryRequest.getCategoryParentID())) {
                return new GenericResponse(HttpStatus.BAD_REQUEST, "Tạo thất bại! ParentID không tồn tại.", null);
            }
        }
        // Thêm mới
        Category category = categoryMapper.toCategory(categoryRequest);
        category = categoryRepository.save(category);
        return new GenericResponse(HttpStatus.OK, "Tạo thành công!", category);

    }

    @Override
    public GenericResponse update(UUID uuid, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(uuid).orElse(null);
        if (category == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Cập nhật thất bại! Không tìm thấy category này.", null);
        }

        if (categoryRequest.getCategoryParentID() != null && !categoryRepository.existsById(categoryRequest.getCategoryParentID())) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Cập nhật thất bại! ParentID không tồn tại.", null);
        }

        category.setName(categoryRequest.getTypeName());
        category.setParentId(categoryRequest.getCategoryParentID());

        category = categoryRepository.save(category);
        return new GenericResponse(HttpStatus.OK,"Cập nhật thành công!", category);
    }

    @Override
    public GenericResponse delete(UUID uuid) {
        Category category = categoryRepository.findById(uuid).orElse(null);
        if (category == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Xoá thất bại! Không tìm thấy category này.", null);
        }
        // Xoá tất cả các product có mã danh mục cần xoá bao gồm cả danh mục con
        List<Category> categoryChilds = categoryRepository.findByParentId(uuid);
        categoryChilds.forEach(categoryChild -> {
            List<Product> products = productRepository.findAllByCategory(category);
            products.forEach(product -> productService.delete(product.getId()));
            categoryRepository.deleteById(categoryChild.getId());
        });

        List<Product> products = productRepository.findAllByCategory(category);
        products.forEach(product -> productService.delete(product.getId()));
        categoryRepository.deleteById(category.getId());

        return new GenericResponse(HttpStatus.OK, "Xoá thành công!", null);
    }
}

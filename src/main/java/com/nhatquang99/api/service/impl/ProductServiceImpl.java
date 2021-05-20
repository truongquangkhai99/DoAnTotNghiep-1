package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.mapper.ProductMapper;
import com.nhatquang99.api.model.Category;
import com.nhatquang99.api.model.Product;
import com.nhatquang99.api.model.ProductImage;
import com.nhatquang99.api.payload.request.ProductRequest;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;
import com.nhatquang99.api.payload.response.ProductResponse;
import com.nhatquang99.api.repository.CategoryRepository;
import com.nhatquang99.api.repository.ProductImageRepository;
import com.nhatquang99.api.repository.ProductRepository;
import com.nhatquang99.api.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    private ProductMapper productMapper = new ProductMapper();

    @Override
    public ListResponse findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        ListResponse response = new ListResponse();
        response.setNumberOfEntities(productRepository.count());
        response.setSizeList(products.getContent().size());
        response.setList(productMapper.toListProductRes(products.getContent()));
        return response;
    }

    @Override
    public ListResponse findByCategoryOrName(String categoryCode, String name, Pageable pageable) {
        List<ProductResponse> productResponses = new ArrayList<>();
        Page<Product> products;

        ListResponse response = new ListResponse();
        response.setNumberOfEntities(productRepository.count());

        //  Trường hợp cả categoryCode và name đều rỗng
        if (categoryCode.equals("") && name.equals("")) {
            response.setSizeList(productResponses.size());
            response.setList(productResponses);
            return response;
        }

        // Trường hợp categoryCode rỗng
        if (categoryCode.equals("")) {
            products = productRepository.findByNameIgnoreCaseContaining(name, pageable);
            response.setSizeList(products.getContent().size());
            response.setList(productMapper.toListProductRes(products.getContent()));
            return response;
        }

        Category category = categoryRepository.findByCode(categoryCode);
        if (category == null) {
            response.setSizeList(productResponses.size());
            response.setList(productResponses);
            return response;
        }
        // Trường hợp category là con
        if (category.getParentId() != null) {
            products = productRepository.findByCategory(category, pageable);
            products.forEach(product -> {
                if (product.getName().contains(name)) {
                    productResponses.add(productMapper.toProductRes(product));
                }
            });
            response.setSizeList(products.getContent().size());
            response.setList(productResponses);
            return response;
        }
        // Trường hợp category là cha
        List<Category> childCategories = categoryRepository.findByParentId(category.getParentId());
        for (Category childCategory : childCategories) {
            products = productRepository.findByCategory(childCategory, pageable);
            products.forEach(product -> {
                if (product.getName().contains(name)) {
                    productResponses.add(productMapper.toProductRes(product));
                }
            });
        }
        response.setSizeList(productResponses.size());
        response.setList(productResponses);
        return response;
    }


    @Override
    @Transactional
    public GenericResponse create(ProductRequest productRequest) {
        String categoryCode = productRequest.getCategoryCode();
        if (!categoryRepository.existsByCode(categoryCode)) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Thêm thất bại! Category không tồn tại.", null);
        }
        Product product = new Product();
        saveProduct(product, productRequest);

        return new GenericResponse(HttpStatus.OK,"Thêm thành công!",null);
    }

    @Override
    @Transactional
    public GenericResponse update(UUID id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST,"Sửa thất bại! Product ID không tồn tại.",null);
        }

        String categoryCode = productRequest.getCategoryCode();
        if (!categoryRepository.existsByCode(categoryCode)) {
            return new GenericResponse(HttpStatus.BAD_REQUEST, "Sửa thất bại! Category không tồn tại.", null);
        }

        saveProduct(product, productRequest);
        return new GenericResponse(HttpStatus.OK,"Sửa thành công!",null);
    }

    @Override
    @Transactional
    public GenericResponse delete(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST,"Xoá thất bại! Product ID không tồn tại.",null);
        }
        deleteProductImage(product);
        productRepository.deleteById(id);
        return new GenericResponse(HttpStatus.OK,"Xoá thành công!",null);
    }

    @Override
    public Object getTotalProduct() {
        Long size = productRepository.count();
        Map<String, Long> map = new HashMap<>();
        map.put("total", size);
        return map;
    }

    @Override
    public GenericResponse getProductById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return new GenericResponse(HttpStatus.BAD_REQUEST,"Thất bại! Product ID không tồn tại.",null);
        }
        ProductResponse productResponse = productMapper.toProductRes(product);
        return new GenericResponse(HttpStatus.OK,"Thành công!", productResponse);
    }


    private void saveProduct(Product product, ProductRequest productRequest) {
        product.setContent(productRequest.getDescription());
        product.setName(productRequest.getNameProduct());
        product.setPrice(productRequest.getPrices());
        product.setQuantity(productRequest.getQuantity());
        product.setType(productRequest.getTypeCode());
        product.setCategory(categoryRepository.findByCode(productRequest.getCategoryCode()));

        product = productRepository.save(product);
        if (product.getListImage() == null) {
            for (String url : productRequest.getImgUrl()) {
                saveProductImage(url, product);
            }
        } else {
            int sizeImg = product.getListImage().size();
            int sizeImgReq = product.getListImage().size();
            for (int i = 0; i < sizeImgReq; i++) {
                String urlRequest = productRequest.getImgUrl().get(i);
                if (sizeImg - (i + 1) >= 0) {
                    ProductImage productImage = product.getListImage().get(i);
                    if (!productImage.getUrl().equals(urlRequest)) {
                        productImage.setUrl(urlRequest);
                        productImageRepository.save(productImage);
                    }
                } else {
                    saveProductImage(urlRequest, product);
                }
            }
        }
    }

    private void saveProductImage(String url, Product product) {
        ProductImage productImage = new ProductImage();
        productImage.setUrl(url);
        productImage.setProduct(product);

        productImageRepository.save(productImage);
    }

    private void deleteProductImage(Product product) {
        List<ProductImage> productImages = productImageRepository.findAllByProduct(product);
        productImages.forEach(productImage -> productImageRepository.delete(productImage));
    }
}

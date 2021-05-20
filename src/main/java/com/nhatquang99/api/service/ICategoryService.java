package com.nhatquang99.api.service;

import com.nhatquang99.api.payload.request.CategoryRequest;
import com.nhatquang99.api.payload.response.CategoryResponse;
import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.payload.response.ListResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ICategoryService {
    ListResponse findAll();
    GenericResponse create(CategoryRequest category);
    GenericResponse update(UUID uuid, CategoryRequest category);
    GenericResponse delete(UUID uuid);
}

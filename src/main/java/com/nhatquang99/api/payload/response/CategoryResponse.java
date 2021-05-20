package com.nhatquang99.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private UUID id;
    private String typeName;
    private String typeCode;
    private List<CategoryResponse> typeList;

    public CategoryResponse() {

    }
}


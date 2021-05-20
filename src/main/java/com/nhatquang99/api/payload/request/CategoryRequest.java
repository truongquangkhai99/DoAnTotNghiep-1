package com.nhatquang99.api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequest {
    private String typeName;
    private String typeCode;
    private UUID categoryParentID;
}

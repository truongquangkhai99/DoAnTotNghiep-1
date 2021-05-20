package com.nhatquang99.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ListResponse {
    private long numberOfEntities;
    private int sizeList;
    private Object list;

    public ListResponse() {

    }
}

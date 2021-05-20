package com.nhatquang99.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class GenericResponse {
    private HttpStatus status;
    private String message;
    private Object dataResponse;

    public GenericResponse() {

    }

}

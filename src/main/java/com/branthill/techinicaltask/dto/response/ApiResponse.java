package com.branthill.techinicaltask.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ApiResponse {

    boolean success;

    long id;

    public ApiResponse(boolean success) {
        this.success = success;
        this.id = 0;
    }

}

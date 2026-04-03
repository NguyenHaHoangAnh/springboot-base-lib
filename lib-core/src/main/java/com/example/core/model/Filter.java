package com.example.core.model;

import com.example.core.constant.ResultCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter implements Serializable {

    @NotNull(message = ResultCode.Code.FILTER_FIELD_NULL)
    @Size(max = 50, message = ResultCode.Code.FILTER_FIELD_MAX_LENGTH)
    private String field;

    @NotNull(message = ResultCode.Code.FILTER_TYPE_NULL)
    @Size(max = 50, message = ResultCode.Code.FILTER_TYPE_MAX_LENGTH)
    private String type;

    @NotNull(message = ResultCode.Code.FILTER_COMPARE_NULL)
    @Size(max = 50, message = ResultCode.Code.FILTER_COMPARE_MAX_LENGTH)
    @Pattern(regexp = "(startsWith|STARTSWITH|endsWith|ENDSWITH|contains|CONTAINS|lt|LT|lte|LTE|gt|GT|gte|GTE|equals|EQUALS|neq|NEQ|in|IN)", message = ResultCode.Code.FILTER_COMPARE_INVALID)
    private String compare;

    public String getCompare() {
        return compare.toLowerCase();
    }

    @NotNull(message = ResultCode.Code.FILTER_VALUE_NULL)
    private Object value;
}
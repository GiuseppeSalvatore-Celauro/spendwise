package com.celauro.SpendWise.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private List<ValidationErrDTO> messages;
    private String message;
    private long timestamp;
    private int status;
    private String error;
    private String path;
    private String method;
}

package com.tpe.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotBlank(message = "provide username")
    @Size(min = 4, max = 25)
    private String userName;

    @NotBlank(message = "provide password")
    @Size(min = 6, max = 25)
    private String password;
}

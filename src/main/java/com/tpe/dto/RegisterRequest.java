package com.tpe.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data // Getter , Setter , AllArgs , NoArgs , toString
public class RegisterRequest {
    @NotBlank(message = "provide first name")
    private String firstName;

    @NotBlank(message = "provide last name")
    private String lastName;

    @NotBlank(message = "provide username")
    @Size(min = 4, max = 25)
    private String userName;

    @NotBlank(message = "provide password")
    @Size(min = 6, max = 25)
    private String password;
}

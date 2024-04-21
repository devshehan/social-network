package com.shehan.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateRequest {

    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is not blank")
    @Email(message = "Email is not formatted")
    private String email;
    @Size(min = 8, message = "Characters should be more than 8 characters")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is not blank")
    private String password;


}

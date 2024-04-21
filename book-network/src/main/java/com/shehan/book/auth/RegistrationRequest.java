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
public class RegistrationRequest {

    @NotEmpty(message = "First name mandatory")
    @NotBlank(message = "First name mandatory")
    private String firstName;
    @NotEmpty(message = "Last name mandatory")
    @NotBlank(message = "Last name mandatory")
    private String lastName;
    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email mandatory")
    @NotBlank(message = "Email mandatory")
    private String email;
    @Size(min = 8, message = "Password should be 8 minimum length")
    @NotEmpty(message = "Password mandatory")
    @NotBlank(message = "Password mandatory")
    private String password;
}

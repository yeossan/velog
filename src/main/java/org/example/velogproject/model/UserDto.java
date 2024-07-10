package org.example.velogproject.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}

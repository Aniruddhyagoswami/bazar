package org.ecommerce.project.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Request object for registering a new user account.")
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    @Schema(description = "Unique username for the new account", example = "john_doe")
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Set of roles assigned to the user (e.g., ROLE_USER, ROLE_ADMIN)",
            example = "[\"ROLE_USER\"]")
    private Set<String> roles;
    @NotBlank
    @Size(min = 6, max = 40)
    @Schema(description = "Password for the user account", example = "StrongP@ssword123")
    private String password;
}

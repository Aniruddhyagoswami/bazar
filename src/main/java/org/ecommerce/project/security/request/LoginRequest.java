package org.ecommerce.project.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "Request object for user authentication containing username and password.")
public class LoginRequest {
    @NotBlank
    @Schema(description = "Username or email used for login", example = "john_doe")
    private String username;


    @NotBlank
    @Schema(description = "Password associated with the user account", example = "StrongP@ssword123")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

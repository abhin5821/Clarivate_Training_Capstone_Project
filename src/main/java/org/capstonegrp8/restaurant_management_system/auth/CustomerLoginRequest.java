package org.capstonegrp8.restaurant_management_system.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Passwordless identify-or-create request for the customer flow.
 * Phone is the unique identifier; name is required; city is optional.
 */
@Data
public class CustomerLoginRequest {

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Name is required")
    private String name;

    private String city;
}

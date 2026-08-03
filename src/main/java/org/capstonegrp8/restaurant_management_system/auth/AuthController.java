package org.capstonegrp8.restaurant_management_system.auth;

import org.capstonegrp8.restaurant_management_system.entity.Customer;
import org.capstonegrp8.restaurant_management_system.repository.ManagerRepository;
import org.capstonegrp8.restaurant_management_system.repository.WaiterRepository;
import org.capstonegrp8.restaurant_management_system.security.JwtUtil;
import org.capstonegrp8.restaurant_management_system.service.CustomerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ManagerRepository managerRepository;
    private final WaiterRepository waiterRepository;
    private final CustomerService customerService;

    /**
     * Email + password login for staff only (MANAGER, WAITER).
     * Customers no longer authenticate this way — see /auth/customer.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .orElseThrow(() -> new RuntimeException("No role assigned"));

        String email = request.getEmail();
        String token = jwtUtil.generateToken(email, role);

        Long userId = null;
        String name = null;

        if ("MANAGER".equals(role)) {
            var manager = managerRepository.findByEmail(email).orElseThrow();
            userId = manager.getManagerId();
            name = manager.getName();
        } else if ("WAITER".equals(role)) {
            var waiter = waiterRepository.findByEmail(email).orElseThrow();
            userId = waiter.getWaiterId();
            name = waiter.getName();
        }

        return new AuthResponse(token, role, userId, name, email, null);
    }

    /**
     * Passwordless customer identify-or-create flow. Phone is the unique
     * identifier: if it already belongs to a customer, that record is reused
     * (name/city refreshed); otherwise a new customer is created on the fly.
     * Always issues a fresh CUSTOMER token — no signup/login screens needed.
     */
    @PostMapping("/customer")
    public AuthResponse customerLogin(@Valid @RequestBody CustomerLoginRequest request) {

        Customer customer = customerService.findOrCreateByPhone(
                request.getPhone(),
                request.getName(),
                request.getCity()
        );

        String token = jwtUtil.generateToken(customer.getPhone(), "CUSTOMER");

        return new AuthResponse(
                token,
                "CUSTOMER",
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
}

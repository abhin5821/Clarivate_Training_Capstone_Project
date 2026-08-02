package org.capstonegrp8.restaurant_management_system.security;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //return NoOpPasswordEncoder.getInstance();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()

                        // Registration
                        .requestMatchers(HttpMethod.POST, "/customers").permitAll()

                        // Manager only
                        .requestMatchers("/managers/**").hasRole("MANAGER")

                        .requestMatchers("/menu-items/**").hasRole("MANAGER")

                        // Waiter + Manager
                        .requestMatchers("/tables/**")
                        .hasAnyRole("WAITER", "MANAGER")

                        .requestMatchers("/waiters/**")
                        .hasAnyRole("WAITER", "MANAGER")

                        .requestMatchers("/orders/**")
                        .hasAnyRole("WAITER", "MANAGER")

                        .requestMatchers("/order-items/**")
                        .hasAnyRole("WAITER", "MANAGER")

                        // Payments
                        .requestMatchers("/payments/**")
                        .hasAnyRole("WAITER", "MANAGER")

                        // Customers
                        .requestMatchers("/customers/**")
                        .hasRole("MANAGER")

                        // Reservations
                        .requestMatchers("/reservations/**")
                        .hasAnyRole("CUSTOMER", "WAITER", "MANAGER")

                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}

package gr.socialsphere.socialsphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ─── 1) Configure CSRF (disabled for JWT-based auth) ───
                .csrf(csrf -> csrf.disable())

                // ─── 2) Authorize requests ───
                .authorizeHttpRequests(authz -> authz
                        // ── 2a) Whitelist endpoints ───
                        .requestMatchers(
                                "/swagger-ui",
                                "/swagger-ui/",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui/oauth2-redirect.html",
                                "/error"
                        ).permitAll()

                        // ── 2b) Whitelist auth and public endpoints ───
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/public/**",
                                "/post/fetch-photo/**"
                        ).permitAll()

                        // ── 2c) All other requests need authentication ───
                        .anyRequest().authenticated()
                )

                // ─── 3) Enable CORS ───
                .cors(Customizer.withDefaults())

                // ─── 4) Configure session management ───
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ─── 5) Configure authentication ───
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

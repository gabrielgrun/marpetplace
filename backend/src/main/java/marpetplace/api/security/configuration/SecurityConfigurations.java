package marpetplace.api.security.configuration;

import marpetplace.api.security.SecurityFilter;
import marpetplace.api.service.AdminAuthenticationService;
import marpetplace.api.service.UsuarioAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private UsuarioAuthenticationService usuarioAuthenticationService;

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http.csrf(AbstractHttpConfigurer::disable)
                        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(req -> {
                            req.requestMatchers("/usuarios/login").permitAll();
                            req.requestMatchers("/admin/login").permitAll();
                            req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
                            req.requestMatchers(HttpMethod.PATCH, "/usuarios/recuperar-senha").permitAll();
                            req.requestMatchers("/admin/**").hasRole("ADMIN");
                            req.requestMatchers("/usuarios/**").hasRole("USER");
                            req.requestMatchers("/common/**").hasAnyRole("ADMIN", "USER");
                            req.requestMatchers(
                                    "/v3/api-docs/**",       // Swagger 3.x
                                    "/bus/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/swagger-ui/**",        // Swagger 3.x
                                    "/v2/api-docs",          // Swagger 2.x
                                    "/webjars/**",
                                    "/swagger-ui.html",      // Swagger 2.x
                                    "/swagger.json",
                                    "/api-docs/**"
                            ).permitAll();
                            req.anyRequest().authenticated();
                        })
                        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean(name = "usuarioAuthenticationManager")
    @Primary
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProviderUsuario()));
    }

    @Bean(name = "adminAuthenticationManager")
    public AuthenticationManager adminAuthenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProviderAdmin()));
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProviderUsuario() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioAuthenticationService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProviderAdmin() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminAuthenticationService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

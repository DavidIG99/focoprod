package com.focoprod.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// @Configuration indica que esta clase define beans de configuración de Spring.
// Más info: https://docs.spring.io/spring-framework/reference/core/beans/java-config.html
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// @EnableWebSecurity habilita la configuración de seguridad web de Spring Security.
// Más info: https://docs.spring.io/spring-security/reference/servlet/architecture.html
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// PasswordEncoder se usa para encriptar contraseñas de forma segura.
// Más info: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
import org.springframework.security.web.SecurityFilterChain;

// Clase de configuración de seguridad.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectamos nuestro CustomOAuth2SuccessHandler para usarlo en la configuración de OAuth2.
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    // Inyección por constructor: Spring pasará la instancia del handler automáticamente.
    public SecurityConfig(CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    // Definimos un bean de tipo SecurityFilterChain.
    // Es la forma moderna (sin extender WebSecurityConfigurerAdapter) de configurar HttpSecurity.
    //
    // Más info: https://docs.spring.io/spring-security/reference/servlet/authentication/index.html#_securityfilterchain_bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF (Cross-Site Request Forgery) para simplificar mientras se desarrolla
                // o si el front es completamente separado y se manejan tokens de otra forma.
                // En producción, suele ser mejor configurarlo correctamente que deshabilitarlo.
                // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html
                .csrf(csrf -> csrf.disable())

                // Habilitamos CORS (Cross-Origin Resource Sharing).
                // Aquí se deja la configuración vacía ({}) porque se suele complementar
                // con un bean CorsConfigurationSource o anotaciones @CrossOrigin.
                // https://docs.spring.io/spring-framework/reference/web/webmvc-cors.html
                .cors(c -> {})

                // Configuramos la autorización de las peticiones HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas que no requieren autenticación.
                        .requestMatchers(
                                "/api/auth/register",
                                "/oauth2/**",
                                "/login/**",
                                "/error"
                        ).permitAll()
                        // Cualquier otra ruta requiere estar autenticado.
                        .anyRequest().authenticated()
                )

                // Configuración del login vía OAuth2 (Google, etc.).
                .oauth2Login(oauth -> oauth
                        // Usamos nuestro handler personalizado cuando el login OAuth2
                        // se realiza con éxito.
                        .successHandler(customOAuth2SuccessHandler)
                )

                // Configuración del logout.
                .logout(logout -> logout
                        // URL para cerrar sesión.
                        .logoutUrl("/logout")
                        // A dónde redirigir después de cerrar sesión.
                        .logoutSuccessUrl("http://localhost:5500/index.html")
                        // Invalida la sesión HTTP.
                        .invalidateHttpSession(true)
                        // Elimina la cookie de sesión de Spring.
                        .deleteCookies("JSESSIONID")
                );

        // Construimos y devolvemos la cadena de filtros de seguridad.
        return http.build();
    }

    // Bean que define el algoritmo de encriptación de contraseñas.
    // BCrypt es una opción recomendada para almacenar contraseñas de forma segura.
    // Más info: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#page-title
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
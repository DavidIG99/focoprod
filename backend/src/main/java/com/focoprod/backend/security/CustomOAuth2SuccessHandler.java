package com.focoprod.backend.security;

import com.focoprod.backend.model.Usuario;
import com.focoprod.backend.repositorios.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

// @Component indica que esta clase es un bean administrado por Spring.
// Spring la detecta en el escaneo de componentes y la inyecta donde haga falta.
// Más info: https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    // Repositorio para acceder y persistir usuarios en la base de datos.
    // Lo usamos para buscar/crear/actualizar un Usuario a partir de los datos de OAuth2.
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias vía constructor.
    // Spring creará una instancia de UsuarioRepository y la pasará aquí automáticamente.
    public CustomOAuth2SuccessHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        System.out.println("✅ [CustomOAuth2SuccessHandler] BEAN CREADO");
    }

    // Implementación del método de AuthenticationSuccessHandler.
    // Este método se ejecuta cuando la autenticación se realiza correctamente.
    //
    // @Transactional garantiza que todas las operaciones sobre la base de datos
    // dentro de este método se ejecuten dentro de una misma transacción:
    //  - Si todo va bien → commit
    //  - Si hay error en medio → rollback
    //
    // Más info @Transactional:
    // https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        System.out.println("✅ [CustomOAuth2SuccessHandler] onAuthenticationSuccess() ENTRÓ");

        // Verificamos que la autenticación realmente provenga de OAuth2.
        // Usamos pattern matching con instanceof (Java 17+):
        // si es OAuth2AuthenticationToken, lo castea y asigna a oauthToken.
        //
        // OAuth2AuthenticationToken representa una autenticación hecha a través de OAuth2
        // (Google, GitHub, etc.).
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            System.out.println("❌ Autenticación no es OAuth2, no hago nada");
            // Aunque no sea OAuth2, redirigimos al mismo success del front.
            response.sendRedirect("http://localhost:5500/success.html");
            return;
        }

        // registrationId es el identificador del cliente OAuth2 configurado en Spring Security,
        // por ejemplo: "google".
        //
        // Más info sobre OAuth2AuthenticationToken:
        // https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/oauth2/client/authentication/OAuth2AuthenticationToken.html
        String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // "google"

        // attrs contiene los "claims"/atributos del usuario devueltos por el proveedor OAuth2.
        // En el caso de Google (OpenID Connect), suele incluir:
        //  - "sub"    → id único del usuario en Google
        //  - "email"  → correo del usuario
        //  - "name"   → nombre completo
        //
        // Más info sobre UserInfo y claims:
        // https://docs.spring.io/spring-security/reference/servlet/oauth2/login/advanced.html
        Map<String, Object> attrs = oauthToken.getPrincipal().getAttributes();

        // "sub" es el identificador único del usuario en el proveedor (OpenID Connect).
        String providerId = (String) attrs.get("sub");          // id de Google
        String email = (String) attrs.get("email");
        String name = (String) attrs.getOrDefault("name", "");

        System.out.println("🔵 OAuth2 success - provider=" + registrationId +
                ", email=" + email +
                ", providerId=" + providerId);

        // Si por alguna razón el proveedor no envía email, no podemos registrar/identificar al usuario.
        // En ese caso redirigimos a la página principal.
        if (email == null || email.isBlank()) {
            System.out.println("❌ No vino email en el token de Google");
            response.sendRedirect("http://localhost:5500/index.html");
            return;
        }

        // Buscamos si ya existe un usuario con ese email en la base de datos.
        //
        // usuarioRepository.findByEmail(email) devuelve un Optional<Usuario>.
        // Usamos map(...) si existe, y orElseGet(...) si no existe:
        //  - Si existe → actualizamos nombre, provider y providerId.
        //  - Si no existe → creamos un nuevo Usuario con esos datos.
        //
        // Esto aprovecha los "query methods" de Spring Data JPA:
        // https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html
        Usuario usuarioGuardado = usuarioRepository.findByEmail(email)
                .map(u -> {
                    System.out.println("🟡 Actualizando usuario existente con email " + email);
                    if (name != null && !name.isBlank()) u.setName(name);
                    u.setProvider(registrationId);   // "google"
                    u.setProviderId(providerId);
                    // save() actualiza el registro en la base de datos.
                    return usuarioRepository.save(u);
                })
                .orElseGet(() -> {
                    System.out.println("🟢 Creando usuario nuevo con email " + email);
                    Usuario nuevo = new Usuario();
                    nuevo.setEmail(email);
                    // Si name viene vacío o null, asignamos un nombre por defecto.
                    nuevo.setName(name != null && !name.isBlank() ? name : "Usuario sin nombre");
                    nuevo.setProvider(registrationId);   // "google"
                    nuevo.setProviderId(providerId);
                    // save() inserta el nuevo usuario en la base de datos.
                    return usuarioRepository.save(nuevo);
                });

        System.out.println("✅ Usuario guardado con id=" + usuarioGuardado.getId());

        // Finalmente, redirigimos al frontend después de haber guardado/actualizado el usuario.
        // Aquí podrías cambiar la URL cuando tengas un front definitivo (React, Vue, etc.).
        response.sendRedirect("http://localhost:5500/success.html");
    }
}
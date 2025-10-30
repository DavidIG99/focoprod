// Paquete al que pertenece esta clase dentro de la estructura del proyecto.
// Más info sobre paquetes en Java:
// https://docs.oracle.com/javase/tutorial/java/package/
package com.focoprod.backend.controller;

// Importamos la anotación @AuthenticationPrincipal, que permite obtener
// el usuario autenticado directamente desde el contexto de seguridad.
// Docs oficiales:
// https://docs.spring.io/spring-security/reference/servlet/authentication/principal.html
import org.springframework.security.core.annotation.AuthenticationPrincipal;

// Importamos la interfaz OAuth2User, que representa a un usuario autenticado
// mediante OAuth 2.0 (por ejemplo Google, GitHub, etc.).
// Docs oficiales:
// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/oauth2/core/user/OAuth2User.html
import org.springframework.security.oauth2.core.user.OAuth2User;

// Importamos las anotaciones necesarias para controlar rutas HTTP.
// Docs oficiales:
// https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController indica que esta clase es un controlador REST.
// Combina @Controller y @ResponseBody.
// Docs:
// https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html#rest-controller
@RestController
public class HomeController {

    // @GetMapping indica que este método responderá a peticiones HTTP GET
    // en la ruta "/home".
    // Docs:
    // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html#webmvc-ann-requestmapping-get
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User user) {

        // @AuthenticationPrincipal permite acceder directamente al usuario autenticado
        // que Spring Security tiene almacenado en el contexto de seguridad.
        // En este caso, será un OAuth2User si el login fue por Google, GitHub, etc.
        //
        // Docs:
        // https://docs.spring.io/spring-security/reference/servlet/authentication/principal.html

        // OAuth2User posee un mapa de atributos que provienen del proveedor OAuth.
        // Ejemplo Google:
        //   name → nombre completo
        //   email → dirección de correo
        //
        // Más info:
        // https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html#oauth2login-userinfo
        return "Bienvenido, " + user.getAttribute("name") + " (" + user.getAttribute("email") + ")";
    }
}
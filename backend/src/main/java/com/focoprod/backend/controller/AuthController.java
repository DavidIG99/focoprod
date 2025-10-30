// Paquete al que pertenece esta clase dentro de la estructura del proyecto.
// Sirve para organizar el código y evitar conflictos de nombres entre clases.
// Más info: https://docs.oracle.com/javase/tutorial/java/package/
package com.focoprod.backend.controller;

import com.focoprod.backend.dto.RegistroRequest;
import com.focoprod.backend.service.UsuarioService;

// Importamos las anotaciones necesarias de Spring para construir un controlador REST.
// Docs oficiales: https://docs.spring.io/spring-framework/reference/web/webmvc/controller.html
import org.springframework.web.bind.annotation.*;

// @RestController indica que esta clase es un controlador REST.
// Combina @Controller y @ResponseBody.
// Más info: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html#rest-controller
@RestController

// @RequestMapping establece la ruta base para todos los endpoints de este controlador.
// Más info: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
@RequestMapping("/api/auth")

// @CrossOrigin habilita CORS para permitir que un frontend externo acceda a este backend.
// Docs oficiales: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-cors.html
@CrossOrigin(origins = "http://localhost:5500") // o donde esté tu front
public class AuthController {

    // Inyección de dependencias: UsuarioService se recibe como dependencia obligatoria.
    // Más info sobre DI en Spring: https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaboration.html
    private final UsuarioService usuarioService;

    // Constructor donde Spring inyecta UsuarioService.
    // Más info sobre constructor injection: https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaboration.html#beans-constructor-injection
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // @PostMapping indica que este método atiende una petición HTTP POST.
    // Docs: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html#webmvc-ann-requestmapping-post
    @PostMapping("/register")
    public String registrarUsuario(@RequestBody RegistroRequest request) {

        // @RequestBody convierte el JSON del cliente en un objeto Java.
        // Más info: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods.html#webmvc-ann-requestbody
        //
        // Se delega al UsuarioService la lógica de registro.
        return usuarioService.registerUser(request);
    }

    // @GetMapping indica que atiende una petición HTTP GET.
    // Más info: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html#webmvc-ann-requestmapping-get
    @GetMapping("/dashboard")
    public String dashboard() {
        // Este método simplemente devuelve un texto.
        // Podría convertirse en un endpoint protegido.
        return "Bienvenido al Dashboard 🚀";
    }
}
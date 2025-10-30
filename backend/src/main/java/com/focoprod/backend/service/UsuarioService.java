package com.focoprod.backend.service;

import com.focoprod.backend.dto.RegistroRequest;
import com.focoprod.backend.model.Usuario;
import com.focoprod.backend.repositorios.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// @Service indica que esta clase forma parte de la capa de servicios.
// Spring la detecta como un bean y la gestiona automáticamente.
// Más info:
// https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html#stereotype-annotations
@Service
public class UsuarioService {

    // Dependencia hacia el repositorio del usuario.
    private final UsuarioRepository usuarioRepository;

    // Dependencia hacia el PasswordEncoder para encriptar contraseñas.
    // Spring inyecta automáticamente una instancia de BCryptPasswordEncoder
    // debido al bean definido en SecurityConfig.
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias por constructor (forma recomendada por Spring).
    // Más info:
    // https://docs.spring.io/spring-framework/reference/core/beans/dependencies.html#beans-constructor-injection
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método que registra un usuario nuevo usando datos de RegistroRequest.
    public String registerUser(RegistroRequest request) {

        // Verifica si ya existe un usuario con el email proporcionado.
        // findByEmail(...) devuelve un Optional, y .isPresent()
        // indica si el usuario ya existe.
        //
        // Más info Optional:
        // https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return "El usuario ya existe";
        }

        // Creamos una nueva instancia Usuario y la llenamos con los datos del request.
        Usuario usuario = new Usuario();
        usuario.setName(request.getName());
        usuario.setEmail(request.getEmail());

        // Encriptamos la contraseña antes de guardarla en la BD.
        // NUNCA se debe guardar la contraseña en texto plano.
        //
        // PasswordEncoder docs oficiales:
        // https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // provider "local" indica que fue un registro manual,
        // a diferencia de OAuth2 (google, apple).
        usuario.setProvider("local");

        // Persistimos el usuario en la base de datos.
        // save() funciona tanto para insertar como para actualizar (upsert).
        usuarioRepository.save(usuario);

        // Retornamos un mensaje simple al controlador.
        return "Usuario registrado correctamente";
    }
}
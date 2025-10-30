package com.focoprod.backend;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Principal;

// @SpringBootApplication es la anotación principal de una aplicación Spring Boot.
// Combina 3 anotaciones:
//
// @Configuration → Indica que esta clase puede definir beans.
// @EnableAutoConfiguration → Spring configura automáticamente componentes comunes (web, JPA, Jackson...)
// @ComponentScan → Busca componentes (@Service, @Controller, @Repository…) en este paquete y subpaquetes.
//
// Documentación oficial:
// https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration
@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    // Método main: punto de entrada de cualquier aplicación Java.
    // Aquí se inicia la aplicación Spring Boot.
    //
    // SpringApplication.run() arranca el servidor web embebido (por defecto Tomcat),
    // inicia el contenedor de Spring, crea los beans, configura la seguridad, JPA, etc.
    //
    // Documentación oficial:
    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-running-your-application
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    // CommandLineRunner es una interfaz de Spring que permite ejecutar código
    // justo después de que la aplicación arranque completamente.
    //
    // Muy útil para:
    //  - insertar datos iniciales
    //  - probar conexiones
    //  - imprimir logs
    //
    // Documentación oficial:
    // https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.spring-application.command-line-runner
    @Override
    public void run(String... args) throws Exception {
        // Actualmente está vacío.
        // Aquí podrías agregar lógica que se ejecute al iniciar la aplicación,
        // por ejemplo: crear un usuario admin o verificar la conexión a la BD.
    }
}
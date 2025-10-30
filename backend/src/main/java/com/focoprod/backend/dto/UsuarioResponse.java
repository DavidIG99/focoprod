// Paquete donde se encuentra esta clase dentro del proyecto.
// Los paquetes ayudan a organizar las clases y evitar conflictos de nombres.
// Más info sobre paquetes en Java:
// https://docs.oracle.com/javase/tutorial/java/package/
package com.focoprod.backend.dto;

// Importamos la anotación @Data de Lombok.
// Esta anotación genera automáticamente getters, setters, toString(), equals(), hashCode()
// y un constructor por defecto.
// Documentación oficial de Lombok:
// https://projectlombok.org/features/Data
import lombok.Data;

// @Data convierte esta clase en un POJO completo sin tener que escribir código repetitivo.
// Esta clase funciona como un DTO para enviar información del usuario al frontend o cliente.
//
// Más info sobre DTOs (Data Transfer Objects):
// https://martinfowler.com/eaaCatalog/dataTransferObject.html
@Data
public class UsuarioResponse {

    // ID único del usuario. Normalmente proviene de la base de datos.
    private Long id;

    // Nombre del usuario. Se envía al cliente para mostrarlo en el frontend.
    private String name;

    // Email del usuario autenticado o registrado.
    private String email;

    // Proveedor OAuth o método de registro (por ejemplo: "google", "local", "github").
    private String provider;
}
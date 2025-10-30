// Paquete donde se encuentra esta clase dentro del proyecto.
// Ayuda a organizar y estructurar el código.
// Más info sobre paquetes en Java:
// https://docs.oracle.com/javase/tutorial/java/package/
package com.focoprod.backend.dto;

// Importamos la anotación @Data de Lombok.
// @Data genera automáticamente: getters, setters, toString(), equals(), hashCode()
// y un constructor vacío.
// Documentación oficial de Lombok:
// https://projectlombok.org/features/Data
import lombok.Data;

// @Data convierte esta clase en un POJO completo sin escribir código repetitivo.
// Esta clase será usada como DTO para recibir datos en el registro de usuarios.
//
// Más info sobre DTO (Data Transfer Object):
// https://martinfowler.com/eaaCatalog/dataTransferObject.html
@Data
public class RegistroRequest {

    // Atributo donde se almacenará el email enviado por el cliente.
    // Será llenado automáticamente por Spring cuando se reciba un JSON.
    private String email;

    // Nombre del usuario que se registra.
    private String name;

    // Contraseña proporcionada en el registro.
    private String password;
}
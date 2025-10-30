// Paquete donde se encuentra esta clase dentro del proyecto.
// Información sobre paquetes en Java:
// https://docs.oracle.com/javase/tutorial/java/package/
package com.focoprod.backend.model;

// Importamos las anotaciones de JPA necesarias para mapear la clase a la base de datos.
// Documentación oficial de JPA (Jakarta Persistence):
// https://jakarta.ee/specifications/persistence/
import jakarta.persistence.*;

// Lombok genera automáticamente getters, setters, equals, hashCode y toString.
// @Data incluye @Getter, @Setter, @RequiredArgsConstructor, @ToString y @EqualsAndHashCode.
// Documentación oficial:
// https://projectlombok.org/features/Data
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
// @Data elimina la necesidad de escribir getters, setters, equals, hashCode y toString.
// Lombok genera todo automáticamente.

// @Entity indica que esta clase es una entidad JPA y se mapeará a una tabla de la BD.
// Más info:
// https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/Entity
@Entity

// @Table permite personalizar el nombre de la tabla en la base de datos.
// Si no se coloca, se usaría el nombre de la clase ("usuario") automáticamente.
// Más info:
// https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/Table
@Table(name = "usuarios")
// Esta clase se mapeará a una tabla llamada "usuarios".
public class Usuario {

    // @Id marca este campo como clave primaria de la tabla.
    // @GeneratedValue indica que su valor se generará automáticamente.
    // GenerationType.IDENTITY significa que usará autoincrement en la base de datos.
    //
    // Más info:
    // https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/GeneratedValue
    // https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column permite personalizar características de una columna.
    // nullable = false → No permite valores nulos.
    // unique = true → No permite valores repetidos (email único).
    //
    // Más info:
    // https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/Column
    @Column(nullable = false, unique = true)
    private String email;

    // name no puede ser null gracias a nullable = false.
    @Column(nullable = false)
    private String name;

    // password puede ser null cuando el usuario se registra con OAuth2.
    private String password;

    // provider indica cómo se registró el usuario: ("local", "google", "apple")
    private String provider;

    // providerId almacena el ID único otorgado por Google/Apple (el "sub").
    private String providerId;

    // Estos campos representan la estructura básica del usuario.
    // - password puede ser null si se registró con OAuth (Google, Apple).
    // - provider indica el origen del registro.
    // - providerId almacena el ID único que provee el servicio OAuth.
}
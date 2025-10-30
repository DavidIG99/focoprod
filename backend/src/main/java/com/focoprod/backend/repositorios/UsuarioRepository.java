// Paquete donde se ubica esta interfaz dentro del proyecto.
// Mantiene el código organizado por capas (modelo, repositorio, servicio, controlador).
// Más info sobre paquetes en Java:
// https://docs.oracle.com/javase/tutorial/java/package/
package com.focoprod.backend.repositorios;

// Importamos la entidad Usuario, que será gestionada por este repositorio.
import com.focoprod.backend.model.Usuario;

// JpaRepository es la interfaz principal en Spring Data JPA para acceder a la BD.
// Provee métodos CRUD listos para usar: save(), findById(), findAll(), deleteById(), etc.
// Documentación oficial:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository marca esta interfaz como un componente de la capa de acceso a datos.
// Maneja excepciones y permite la inyección del repositorio en servicios.
// Documentación oficial:
// https://docs.spring.io/spring-framework/reference/data-access/orm/annotations.html#repository-annotation
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository convierte esta interfaz en un bean de Spring responsable de la persistencia.
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su email.
    // Optional evita NullPointerException y obliga a manejar la ausencia del usuario.
    //
    // Este método usa consultas derivadas ("Query Methods"):
    // Spring Data JPA genera la consulta automáticamente por el nombre del método.
    // Más info:
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    Optional<Usuario> findByEmail(String email);

    // Busca un usuario por su proveedor (google, apple, local)
    // y su ID interno en el proveedor (sub de Google, por ejemplo).
    //
    // Muy útil para logins OAuth2.
    Optional<Usuario> findByProviderAndProviderId(String provider, String providerId);
}
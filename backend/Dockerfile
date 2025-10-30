# Etapa 1: construir el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS builder
# Usamos una imagen oficial de Maven con JDK 21 (Eclipse Temurin).
# Esta etapa sirve SOLO para compilar el proyecto.
# Docs oficiales:
# https://hub.docker.com/_/maven

WORKDIR /app
# Establece el directorio de trabajo dentro del contenedor.

# Copia el pom.xml y descarga dependencias (caché)
COPY pom.xml .
RUN mvn dependency:go-offline
# mvn dependency:go-offline descarga todas las dependencias declaradas en el pom.xml.
# Esto permite usar la caché y acelera compilaciones posteriores.
# Más info:
# https://maven.apache.org/plugins/maven-dependency-plugin/go-offline-mojo.html

# Copia el código fuente y compila
COPY src ./src
RUN mvn package -DskipTests
# Compila el proyecto y genera el .jar dentro de target/.
# -DskipTests evita ejecutar tests para acelerar el build.

# Etapa 2: ejecutar la app
FROM eclipse-temurin:21-jdk
# Imagen final, ligera, solo con JDK para ejecutar la app.
# Docs:
# https://hub.docker.com/_/eclipse-temurin

WORKDIR /app

# Copiamos el .jar generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar
# Extraemos SOLO el .jar ya compilado desde la etapa "builder".
# Esto hace el contenedor más ligero (multistage build).
# Más info:
# https://docs.docker.com/build/building/multi-stage/

EXPOSE 8083
# Informa que el contenedor escucha en el puerto 8083.
# NO abre el puerto, solo documenta.

ENTRYPOINT ["java", "-jar", "app.jar"]
# Comando por defecto: inicia la aplicación Spring Boot.
# ENTRYPOINT asegura que siempre se ejecutará este comando.
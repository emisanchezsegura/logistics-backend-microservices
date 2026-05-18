# Imagen base de OpenJDK
FROM eclipse-temurin:21-jdk-alpine


# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el JAR generado por Maven al contenedor
COPY target/servicio-depositos-1.0-SNAPSHOT.jar app.jar

# Exponemos el puerto que usa el microservicio
EXPOSE 8085

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

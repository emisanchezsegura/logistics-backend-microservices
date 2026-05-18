# Imagen base de OpenJDK
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar generado por Maven al contenedor
COPY target/servicio-usuarios-1.0-SNAPSHOT.jar app.jar

# Exponemos el puerto del servicio
EXPOSE 8090

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]

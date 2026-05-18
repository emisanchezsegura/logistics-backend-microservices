# Imagen base de Java
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar generado por Maven al contenedor
COPY target/*.jar app.jar

# Copiamos también el archivo de configuración application.yml
COPY src/main/resources/application.yml /app/application.yml

# Exponemos el puerto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]

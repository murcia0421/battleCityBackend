# Usar una imagen base de Java
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar generado en el contenedor
COPY target/*.jar /app/backend.jar

# Exponer el puerto en el que corre la aplicación (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "backend.jar"]

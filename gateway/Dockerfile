# 🧱 1. Imagen base (Java)
FROM eclipse-temurin:17-jdk-alpine

# 📁 2. Carpeta dentro del contenedor
WORKDIR /app

# 📦 3. Copiar el .jar generado
COPY target/*.jar app.jar

# 🚀 4. Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /target/ride-share.jar app.jar
EXPOSE 9099
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:23-jdk
COPY client/dist/* resources/static
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 80

ENTRYPOINT ["java","-jar","/app.jar","--server.port=80"]
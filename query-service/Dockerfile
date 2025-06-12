FROM openjdk:21-jdk
ARG JAR_FILE=docker-build/query-service.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
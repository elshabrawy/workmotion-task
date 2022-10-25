FROM maven:3.8.3-openjdk-11
FROM openjdk:11
COPY target/*.jar statemachine-app.jar
ENTRYPOINT ["java","-jar","/statemachine-app.jar"]
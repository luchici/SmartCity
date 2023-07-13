FROM amazoncorretto:11-alpine-jdk
MAINTAINER luchici.github.com
WORKDIR /app
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} .
COPY target/CityService-0.0.1-SNAPSHOT.jar .
EXPOSE 8081
CMD ["java","-jar","CityService-0.0.1-SNAPSHOT.jar"]


#COPY .mvn .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#CMD ["./mvnw", "spring-boot:run"]

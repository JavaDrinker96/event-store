#
# Build stage
#
FROM maven:3.8.6-amazoncorretto-11 AS build
WORKDIR /usr/app
COPY ../src ./src
COPY ../pom.xml .
RUN mvn clean install -Dmaven.test.skip=true
#
# Package stage
#
FROM amazoncorretto:11.0.16
ARG path=/usr/app
WORKDIR ${path}
COPY --from=build ${path}/target/*.jar ${path}/app.jar

CMD [ "sh", "-c", "java -jar app.jar"]
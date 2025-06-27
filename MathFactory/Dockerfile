FROM openjdk:21-jdk-slim
LABEL authors="marko"
COPY "build/libs/MathFactory-0.0.1-SNAPSHOT.jar" "/mathfactory.jar"


ENTRYPOINT ["java", "-jar", "/mathfactory.jar"]
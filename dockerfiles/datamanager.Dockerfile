FROM openjdk:21-jdk-slim
LABEL authors="marko"
COPY "../bootjars/DataManager-0.0.1-SNAPSHOT.jar" "/datamanager.jar"


ENTRYPOINT ["java", "-jar", "/datamanager.jar"]
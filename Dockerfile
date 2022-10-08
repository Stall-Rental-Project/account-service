FROM openjdk:11

# Set the working directory
WORKDIR /app

COPY /target/account-service.jar account-service.jar

ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar", "account-service.jar"]

EXPOSE 9565
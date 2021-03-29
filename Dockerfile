FROM openjdk:11
ADD build/libs/webserver-0.0.1-SNAPSHOT.jar docker-spring-boot.jar
EXPOSE 5000
CMD [ "java", "-jar", "docker-spring-boot.jar" ]
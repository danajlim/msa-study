FROM amazoncorretto:17
LABEL maintainer="dev@welab.com"
VOLUME /tmp
EXPOSE 8888
COPY build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
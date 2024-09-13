FROM openjdk:17-alpine
WORKDIR /app
COPY ./build/libs/*.jar /app/app.jar
EXPOSE 8088
ENTRYPOINT ["sh", "-c", "java -Djasypt.encryptor.password=$JAVA_OPTS -jar /app/app.jar"]

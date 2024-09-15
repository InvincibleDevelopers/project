FROM openjdk:17-alpine
COPY ./build/libs/*.jar /app.jar
EXPOSE 8088
ENTRYPOINT ["sh", "-c", "java -Djasypt.encryptor.password=$JAVA_OPTS -jar /app.jar"]

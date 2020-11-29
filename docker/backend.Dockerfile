#############################################
# Build-stage                               #
#############################################
FROM jbeef/ubuntu-java8:latest

RUN apt-get update; exit 0
RUN apt-get install -y protobuf-c-compiler protobuf-compiler protobuf-compiler-grpc

# Copy source to container
RUN mkdir /backend
COPY ["./backend", "/backend"]

# Build the application
WORKDIR /backend
RUN ./gradlew bootJar --no-daemon

#############################################
# Execute-stage                             #
#############################################
FROM openjdk:8-jre

# Copy jar to container
RUN mkdir /app
COPY --from=0 "/backend/build/libs/*.jar" "/app/caffstore.jar"

WORKDIR /app
ENTRYPOINT ["java", "-jar", "caffstore.jar"]

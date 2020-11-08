#############################################
# Build-stage                               #
#############################################
FROM jbeef/ubuntu-java8:latest

RUN apt-get update; exit 0
RUN apt-get install -y lcov libcppunit-dev build-essential protobuf-compiler

# Copy source to container
RUN mkdir /src
COPY [".", "/src"]

# Build the application
WORKDIR /src/native
RUN make all

WORKDIR /src/native-component
RUN ./gradlew jar --no-daemon

#############################################
# Execute-stage                             #
#############################################
FROM openjdk:8-jre

# Copy jar to container
RUN mkdir /app
COPY --from=0 "/src/native/bin/libcaffparser.so" "/app/libcaffparser.so"
COPY --from=0 "/src/native/bin/libcaffparserjava.so" "/app/libcaffparserjava.so"
COPY --from=0 "/src/native/bin/caffparser.jar" "/app/caffparser.jar"
COPY --from=0 "/src/native-component/build/libs/*.jar" "/app/caffparserserver.jar"

WORKDIR /app
ENV LD_LIBRARY_PATH=.
ENTRYPOINT ["java", "-jar", "caffparserserver.jar"]

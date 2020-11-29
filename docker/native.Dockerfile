#############################################
# Build-stage native                        #
#############################################
FROM jbeef/ubuntu-java8:latest

RUN apt-get update; exit 0
RUN apt-get install -y lcov libcppunit-dev build-essential

# Copy source to container
RUN mkdir /native
COPY ["./native", "/native"]

# Build the application
WORKDIR /native
RUN make all

#############################################
# Build-stage native-component              #
#############################################
FROM jbeef/ubuntu-java8:latest

RUN apt-get update; exit 0
RUN apt-get install -y protobuf-compiler

# Copy source to container
RUN mkdir /native-component
COPY ["./native-component", "/native-component"]

RUN mkdir -p /native/bin
COPY --from=0 "/native/bin/libcaffparser.so" "/native/bin/libcaffparser.so"
COPY --from=0 "/native/bin/libcaffparserjava.so" "/native/bin/libcaffparserjava.so"
COPY --from=0 "/native/bin/caffparser.jar" "/native/bin/caffparser.jar"

WORKDIR /native-component
RUN ./gradlew jar --no-daemon

#############################################
# Execute-stage                             #
#############################################
FROM openjdk:8-jre

# Copy jar to container
RUN mkdir /app
COPY --from=0 "/native/bin/libcaffparser.so" "/app/libcaffparser.so"
COPY --from=0 "/native/bin/libcaffparserjava.so" "/app/libcaffparserjava.so"
COPY --from=0 "/native/bin/caffparser.jar" "/app/caffparser.jar"
COPY --from=1 "/native-component/build/libs/*.jar" "/app/caffparserserver.jar"

WORKDIR /app
ENV LD_LIBRARY_PATH=.
ENTRYPOINT ["java", "-jar", "caffparserserver.jar"]

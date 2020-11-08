# Native component - Java binding

This component provides a service interface for the native code through gRPC. The component accesses the native functionality through JNI.

## Building instructions

To build the project first the native code must be built:

```shell
make all
```

After that, the Java binding can be built:

```shell
./gradlew build assemble
```

## Running the component - Gradle

To start the server capable of serving the requests to the native component via gRPC, issue:

```shell
./gradlew run
```

The gRPC server will be started on potn 50051.

## Running the component - Docker

There is a Dockerfile in the docker folder that can be used to run the server isolated from the host. To build and run the image issue the following commands from the root of the repository:

```shell
docker build . -f docker/native.Dockerfile -t native:latest
docker run --rm -p 50051:50051 native:latest
```

After that, the gRPC implementation will be available on port 50051.

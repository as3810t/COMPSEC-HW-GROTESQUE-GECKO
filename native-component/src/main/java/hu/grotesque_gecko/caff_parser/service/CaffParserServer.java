package hu.grotesque_gecko.caff_parser.service;

import com.google.protobuf.ByteString;
import hu.grotesque_gecko.caff_parser.CAFF;
import hu.grotesque_gecko.caff_parser.service.CaffParserOuterClass.PreviewRequest;
import hu.grotesque_gecko.caff_parser.service.CaffParserOuterClass.PreviewResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class CaffParserServer {

    private Server server;

    public void start() throws IOException {
        final int port = 50051;
        server = ServerBuilder.forPort(port)
            .addService(new CaffParserImpl())
            .build()
            .start();

        System.out.println("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.out.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                CaffParserServer.this.stop();
            }
            catch(InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.out.println("*** server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final CaffParserServer server = new CaffParserServer();
        server.start();
        server.blockUntilShutdown();
    }

    private static class CaffParserImpl extends CaffParserGrpc.CaffParserImplBase {
        @Override
        public void previewCaff(PreviewRequest request, StreamObserver<PreviewResponse> responseObserver) {
            System.out.print("Starting processing bmp... ");
            final ByteBuffer buffer = ByteBuffer.allocateDirect(request.getCaff().size());
            request.getCaff().copyTo(buffer);

            try(final CAFF caff = CAFF.from(buffer)) {
                final PreviewResponse response = PreviewResponse.newBuilder()
                    .setBmp(ByteString.copyFrom(caff.generatePreview()))
                    .build();

                System.out.println("OK");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            catch(Throwable t) {
                System.out.println("ERROR");
                t.printStackTrace();

                final PreviewResponse response = PreviewResponse.newBuilder()
                    .setErrorMessage(t.getMessage())
                    .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }
    }
}

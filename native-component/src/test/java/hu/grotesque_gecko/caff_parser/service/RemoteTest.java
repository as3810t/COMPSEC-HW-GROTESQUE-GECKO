package hu.grotesque_gecko.caff_parser.service;

import com.google.protobuf.ByteString;
import hu.grotesque_gecko.caff_parser.service.CaffParserGrpc.CaffParserBlockingStub;
import hu.grotesque_gecko.caff_parser.service.CaffParserOuterClass.PreviewRequest;
import hu.grotesque_gecko.caff_parser.service.CaffParserOuterClass.PreviewResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class RemoteTest {
    private static CaffParserServer server;

    @BeforeClass
    public static void setupServer() throws IOException {
        server = new CaffParserServer();
        server.start();
    }

    @AfterClass
    public static void tearDownServer() throws InterruptedException {
        server.stop();
        server.blockUntilShutdown();
    }

    @Test
    public void testCaff() throws InterruptedException, IOException {
        final ByteBuffer buffer = readFile("src/test/resources/test.caff");
        final ByteBuffer bmpBuffer = readFile("src/test/resources/test.bmp");

        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:50051")
            .usePlaintext(true)
            .build();

        try {
            final CaffParserClient client = new CaffParserClient(channel);
            Assert.assertEquals(bmpBuffer, client.generatePreview(buffer));
        }
        finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static ByteBuffer readFile(final String filePath) throws IOException {
        final RandomAccessFile file = new RandomAccessFile(filePath,"r");

        final FileChannel inChannel = file.getChannel();
        final long fileSize = inChannel.size();

        final ByteBuffer buffer = ByteBuffer.allocateDirect((int) fileSize);
        inChannel.read(buffer);
        buffer.flip();

        inChannel.close();
        file.close();

        return buffer;
    }
}

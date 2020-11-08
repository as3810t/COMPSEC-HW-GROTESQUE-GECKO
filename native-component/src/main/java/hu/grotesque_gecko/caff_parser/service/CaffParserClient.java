package hu.grotesque_gecko.caff_parser.service;

import com.google.protobuf.ByteString;
import io.grpc.Channel;

import java.nio.ByteBuffer;

public class CaffParserClient {
    private final CaffParserGrpc.CaffParserBlockingStub blockingStub;

    public CaffParserClient(final Channel channel) {
        blockingStub = CaffParserGrpc.newBlockingStub(channel);
    }

    public ByteBuffer generatePreview(final ByteBuffer buffer) {
        final CaffParserOuterClass.PreviewRequest request = CaffParserOuterClass.PreviewRequest.newBuilder()
                .setCaff(ByteString.copyFrom(buffer))
                .build();

        final CaffParserOuterClass.PreviewResponse response = blockingStub.previewCaff(request);
        return response.getBmp().asReadOnlyByteBuffer();
    }
}

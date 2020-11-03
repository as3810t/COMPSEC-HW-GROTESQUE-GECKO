package hu.grotesque_gecko.caff_parser;
import hu.grotesque_gecko.caff_parser.CAFF;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NativeTest {
    @Test
    public void testCaff() throws IOException {
        final ByteBuffer buffer = readFile("src/test/resources/test.caff");
        final ByteBuffer bmpBuffer = readFile("src/test/resources/test.bmp");

        try(final CAFF caff = CAFF.from(buffer)) {
            Assert.assertEquals(bmpBuffer, caff.generatePreview());
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

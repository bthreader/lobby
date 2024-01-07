package lobby.matching.engine.infra;

import lombok.Setter;
import org.agrona.DirectBuffer;

import java.io.IOException;
import java.nio.channels.SocketChannel;

@Setter
public class SessionContextImpl implements SessionContext {
    private SocketChannel client;

    @Override
    public void reply(final DirectBuffer buffer, final int offset, final int length) {
        try {
            // TODO .byteBuffer() may not work
            client.write(buffer.byteBuffer());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}

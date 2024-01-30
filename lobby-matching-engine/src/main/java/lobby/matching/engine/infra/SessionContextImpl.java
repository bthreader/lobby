package lobby.matching.engine.infra;

import lombok.Setter;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Setter
public class SessionContextImpl implements SessionContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionContextImpl.class);
    private SelectionKey selectionKey;
    private SocketChannel client;

    @Override
    public void reply(final DirectBuffer buffer, final int offset, final int length) {
        try {
            final int bytesWritten = client.write(buffer.byteBuffer());

            // Slow client -> disconnect them
            if (bytesWritten < length) {
                LOGGER.warn("Disconnecting slow client {}", client.getRemoteAddress());
                client.close();
                selectionKey.cancel();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SocketAddress remoteAddress() {
        try {
            return client.getRemoteAddress();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}

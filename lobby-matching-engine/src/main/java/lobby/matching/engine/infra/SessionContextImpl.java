package lobby.matching.engine.infra;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Setter
@Slf4j
public class SessionContextImpl implements SessionContext {
    private SelectionKey selectionKey;
    private SocketChannel client;

    @Override
    public void reply(final DirectBuffer buffer, final int offset, final int length) {
        try {
            final int bytesWritten = client.write(buffer.byteBuffer()
                                                          .position(offset)
                                                          .limit(length));

            // Slow client -> disconnect them
            if (bytesWritten < length) {
                log.warn("Disconnecting slow client: {}", client.getRemoteAddress());
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

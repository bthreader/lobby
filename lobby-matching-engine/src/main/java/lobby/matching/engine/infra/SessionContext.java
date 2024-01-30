package lobby.matching.engine.infra;

import org.agrona.DirectBuffer;

import java.net.SocketAddress;

public interface SessionContext {
    /**
     * Replies to the caller.
     *
     * @param buffer the buffer to read data from
     * @param offset the offset to read from
     * @param length the length to read
     */
    void reply(DirectBuffer buffer, int offset, int length);

    SocketAddress remoteAddress();
}

package lobby.core;

import org.agrona.DirectBuffer;

public interface DeMuxer {
    void dispatch(final DirectBuffer buffer, final int offset, final int length);
}

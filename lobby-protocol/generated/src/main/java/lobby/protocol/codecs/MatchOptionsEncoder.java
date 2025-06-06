/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.protocol.codecs;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class MatchOptionsEncoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final int ENCODED_LENGTH = 1;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private MutableDirectBuffer buffer;

    public MatchOptionsEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public static int gameModeEncodingOffset()
    {
        return 0;
    }

    public static int gameModeEncodingLength()
    {
        return 1;
    }

    public MatchOptionsEncoder gameMode(final GameMode value)
    {
        buffer.putByte(offset + 0, (byte)value.value());
        return this;
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final MatchOptionsDecoder decoder = new MatchOptionsDecoder();
        decoder.wrap(buffer, offset);

        return decoder.appendTo(builder);
    }
}

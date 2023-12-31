/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.message.codecs;

import org.agrona.MutableDirectBuffer;


/**
 * A request to put the users in a lobby into another lobby that contains at least one other user
 */
@SuppressWarnings("all")
public final class MergeRequestEncoder
{
    public static final int BLOCK_LENGTH = 5;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final MergeRequestEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int initialOffset;
    private int offset;
    private int limit;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int initialOffset()
    {
        return initialOffset;
    }

    public int offset()
    {
        return offset;
    }

    public MergeRequestEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public MergeRequestEncoder wrapAndApplyHeader(
        final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder)
    {
        headerEncoder
            .wrap(buffer, offset)
            .blockLength(BLOCK_LENGTH)
            .templateId(TEMPLATE_ID)
            .schemaId(SCHEMA_ID)
            .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int lobbyIdId()
    {
        return 1;
    }

    public static int lobbyIdSinceVersion()
    {
        return 0;
    }

    public static int lobbyIdEncodingOffset()
    {
        return 0;
    }

    public static int lobbyIdEncodingLength()
    {
        return 4;
    }

    public static String lobbyIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long lobbyIdNullValue()
    {
        return 4294967295L;
    }

    public static long lobbyIdMinValue()
    {
        return 0L;
    }

    public static long lobbyIdMaxValue()
    {
        return 4294967294L;
    }

    public MergeRequestEncoder lobbyId(final long value)
    {
        buffer.putInt(offset + 0, (int)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }


    public static int gameModeId()
    {
        return 2;
    }

    public static int gameModeSinceVersion()
    {
        return 0;
    }

    public static int gameModeEncodingOffset()
    {
        return 4;
    }

    public static int gameModeEncodingLength()
    {
        return 1;
    }

    public static String gameModeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public MergeRequestEncoder gameMode(final GameMode value)
    {
        buffer.putByte(offset + 4, (byte)value.value());
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

        final MergeRequestDecoder decoder = new MergeRequestDecoder();
        decoder.wrap(buffer, initialOffset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}

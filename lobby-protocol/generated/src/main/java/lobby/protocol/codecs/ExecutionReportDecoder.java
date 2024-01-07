/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.protocol.codecs;

import org.agrona.DirectBuffer;


/**
 * The result of a request
 */
@SuppressWarnings("all")
public final class ExecutionReportDecoder
{
    public static final int BLOCK_LENGTH = 6;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExecutionReportDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int initialOffset;
    private int offset;
    private int limit;
    int actingBlockLength;
    int actingVersion;

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

    public DirectBuffer buffer()
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

    public ExecutionReportDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public ExecutionReportDecoder wrapAndApplyHeader(
        final DirectBuffer buffer,
        final int offset,
        final MessageHeaderDecoder headerDecoder)
    {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId)
        {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
            buffer,
            offset + MessageHeaderDecoder.ENCODED_LENGTH,
            headerDecoder.blockLength(),
            headerDecoder.version());
    }

    public ExecutionReportDecoder sbeRewind()
    {
        return wrap(buffer, initialOffset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength()
    {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
    }

    public int actingVersion()
    {
        return actingVersion;
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

    public static int statusId()
    {
        return 1;
    }

    public static int statusSinceVersion()
    {
        return 0;
    }

    public static int statusEncodingOffset()
    {
        return 0;
    }

    public static int statusEncodingLength()
    {
        return 1;
    }

    public static String statusMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public short statusRaw()
    {
        return ((short)(buffer.getByte(offset + 0) & 0xFF));
    }

    public ExecutionStatus status()
    {
        return ExecutionStatus.get(((short)(buffer.getByte(offset + 0) & 0xFF)));
    }


    public static int failureReasonId()
    {
        return 2;
    }

    public static int failureReasonSinceVersion()
    {
        return 0;
    }

    public static int failureReasonEncodingOffset()
    {
        return 1;
    }

    public static int failureReasonEncodingLength()
    {
        return 1;
    }

    public static String failureReasonMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public short failureReasonRaw()
    {
        return ((short)(buffer.getByte(offset + 1) & 0xFF));
    }

    public ExecutionFailureReason failureReason()
    {
        return ExecutionFailureReason.get(((short)(buffer.getByte(offset + 1) & 0xFF)));
    }


    public static int lobbyIdId()
    {
        return 3;
    }

    public static int lobbyIdSinceVersion()
    {
        return 0;
    }

    public static int lobbyIdEncodingOffset()
    {
        return 2;
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

    public long lobbyId()
    {
        return (buffer.getInt(offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF_FFFFL);
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final ExecutionReportDecoder decoder = new ExecutionReportDecoder();
        decoder.wrap(buffer, initialOffset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(initialOffset + actingBlockLength);
        builder.append("[ExecutionReport](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("status=");
        builder.append(this.status());
        builder.append('|');
        builder.append("failureReason=");
        builder.append(this.failureReason());
        builder.append('|');
        builder.append("lobbyId=");
        builder.append(this.lobbyId());

        limit(originalLimit);

        return builder;
    }
    
    public ExecutionReportDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}

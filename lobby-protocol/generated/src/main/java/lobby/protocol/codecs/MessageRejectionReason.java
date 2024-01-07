/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.protocol.codecs;

@SuppressWarnings("all")
public enum MessageRejectionReason
{
    MESSAGE_TOO_SHORT((short)0),

    UNKNOWN_MESSAGE_TEMPLATE((short)1),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((short)255);

    private final short value;

    MessageRejectionReason(final short value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public short value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static MessageRejectionReason get(final short value)
    {
        switch (value)
        {
            case 0: return MESSAGE_TOO_SHORT;
            case 1: return UNKNOWN_MESSAGE_TEMPLATE;
            case 255: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

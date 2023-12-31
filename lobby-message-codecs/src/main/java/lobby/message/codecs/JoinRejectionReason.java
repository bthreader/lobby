/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.message.codecs;

@SuppressWarnings("all")
public enum JoinRejectionReason
{
    UNKNOWN_LOBBY((short)0),

    ALL_LOBBIES_FULL((short)1),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((short)255);

    private final short value;

    JoinRejectionReason(final short value)
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
    public static JoinRejectionReason get(final short value)
    {
        switch (value)
        {
            case 0: return UNKNOWN_LOBBY;
            case 1: return ALL_LOBBIES_FULL;
            case 255: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

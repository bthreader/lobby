/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.protocol.codecs;

@SuppressWarnings("all")
public enum ExecutionFailureReason
{
    UNKNOWN_LOBBY((short)0),

    ALL_LOBBIES_FULL((short)1),

    USER_ALREADY_IN_LOBBY((short)2),

    CANNOT_DELETE_NON_EMPTY_LOBBY((short)3),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((short)255);

    private final short value;

    ExecutionFailureReason(final short value)
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
    public static ExecutionFailureReason get(final short value)
    {
        switch (value)
        {
            case 0: return UNKNOWN_LOBBY;
            case 1: return ALL_LOBBIES_FULL;
            case 2: return USER_ALREADY_IN_LOBBY;
            case 3: return CANNOT_DELETE_NON_EMPTY_LOBBY;
            case 255: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

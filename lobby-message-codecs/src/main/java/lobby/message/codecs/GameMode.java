/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.message.codecs;

@SuppressWarnings("all")
public enum GameMode
{
    CAPTURE_THE_FLAG(1),

    FREE_FOR_ALL(2),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL(-2147483648);

    private final int value;

    GameMode(final int value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public int value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static GameMode get(final int value)
    {
        switch (value)
        {
            case 1: return CAPTURE_THE_FLAG;
            case 2: return FREE_FOR_ALL;
            case -2147483648: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

/* Generated SBE (Simple Binary Encoding) message codec. */
package lobby.protocol.codecs;

@SuppressWarnings("all")
public enum GameMode
{
    CAPTURE_THE_FLAG((short)1),

    FREE_FOR_ALL((short)2),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((short)255);

    private final short value;

    GameMode(final short value)
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
    public static GameMode get(final short value)
    {
        switch (value)
        {
            case 1: return CAPTURE_THE_FLAG;
            case 2: return FREE_FOR_ALL;
            case 255: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

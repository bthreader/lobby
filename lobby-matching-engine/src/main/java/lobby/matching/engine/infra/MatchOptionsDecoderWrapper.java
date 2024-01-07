package lobby.matching.engine.infra;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.GameMode;
import lobby.protocol.codecs.MatchOptionsDecoder;

/**
 * Encapsulates a MatchOptionsDecoder to hide the visibility of non-domain fields (e.g. offsets).
 */
public class MatchOptionsDecoderWrapper implements MatchOptions {
    private MatchOptionsDecoder matchOptionsDecoder;

    public void wrap(final MatchOptionsDecoder matchOptionsDecoder) {
        this.matchOptionsDecoder = matchOptionsDecoder;
    }

    @Override
    public GameMode gameMode() {
        return matchOptionsDecoder.gameMode();
    }
}

package lobby.protocol;

import lobby.protocol.codecs.GameMode;

/**
 * The criteria for the match.
 */
public interface MatchOptions {
    GameMode gameMode();
}

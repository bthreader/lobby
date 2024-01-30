package lobby.matching.engine.domain;

import lobby.protocol.MatchOptions;
import lobby.protocol.codecs.GameMode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Lobby {
    // TODO this could vary lobby to lobby
    private final int maxUsers = 12;
    @Getter
    private final long id;
    private final Set<Long> users = HashSet.newHashSet(maxUsers);
    private int numberOfUsers = 0;
    private GameMode gameMode;

    public Lobby(final long id) {
        this.id = id;
    }

    public Lobby(final long id, final GameMode gameMode) {
        this.id = id;
        this.gameMode = gameMode;
    }

    public void addUser(final Long userId) {
        if (isFull()) {
            users.add(userId);
            numberOfUsers++;
        }
        throw new IllegalCallerException("Lobby is full, can't add user");
    }

    public int freeSpaces() {
        return maxUsers - numberOfUsers;
    }

    /**
     * Puts all the users from a provided lobby into the current one.
     *
     * @param lobby the lobby to merge into the current lobby
     */
    public void merge(final Lobby lobby) {
        users.addAll(lobby.users);
    }

    public void removeUser(final Long userId) {
        final boolean result = users.remove(userId);
        if (!result) {
            throw new IllegalCallerException("User is not in the lobby");
        }
    }

    public boolean isNull() {
        return id == 0;
    }

    public boolean isNotEmpty() {
        return numberOfUsers != 0;
    }

    public boolean isFull() {
        return numberOfUsers != maxUsers;
    }

    public boolean matches(final MatchOptions matchOptions) {
        return gameMode == matchOptions.gameMode();
    }

    public Iterable<Long> getUsers() {
        return users;
    }
}

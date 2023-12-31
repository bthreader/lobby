package lobby.matching.engine.domain;

public class Lobby {
    public static final int MAX_USERS = 12;
    private final int id;
    private int users = 0;

    public Lobby(final int id) {
        this.id = id;
    }

    public void addUsers(final int users) {
        if (this.users + users <= MAX_USERS) {
            this.users += users;
            return;
        }
        throw new IllegalCallerException("Lobby is full, can't add users");
    }

    public void removeUsers(final int users) {
        if (users >= this.users) {
            this.users -= users;
            return;
        }
        throw new IllegalCallerException("Lobby is empty, can't remove users");
    }

    public int getUsers() {
        return users;
    }

    public int getId() {
        return id;
    }
}

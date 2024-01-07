package lobby.gateway;

public class App {
    public static void main(final String[] args) {
        System.out.println(new App().getGreeting());
    }

    public String getGreeting() {
        return "Hello World!";
    }
}

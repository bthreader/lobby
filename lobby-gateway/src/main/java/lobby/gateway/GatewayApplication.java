package lobby.gateway;

import lobby.protocol.ImmutableMatchOptions;
import lobby.protocol.codecs.GameMode;

import java.util.Scanner;

public class GatewayApplication {
    public static void main(final String[] args) {
        final MatchingEngineClient matchingEngineClient = new MatchingEngineClientImpl();
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to an interactive gateway CLI");
        System.out.println("Enter a command (type 'exit' to quit, 'help' for commands):");

        while (true) {
            final String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program...");
                matchingEngineClient.disconnect();
                break;
            }

            switch (input.toLowerCase()) {
                case "match":
                    matchingEngineClient.matchRequest(new ImmutableMatchOptions(GameMode.CAPTURE_THE_FLAG));
                case "merge":
                    matchingEngineClient.mergeRequest(1,
                                                      new ImmutableMatchOptions(GameMode.CAPTURE_THE_FLAG));
                case "help":
                    System.out.println("match\tsend a matchRequest");
                    System.out.println("merge\tsend a mergeRequest");
                default:
                    System.out.println(
                            "Command '" + input + "' not registered type 'help' for commands");
            }
        }

        scanner.close();
    }
}

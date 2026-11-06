package client;

import java.util.Scanner;

public class PreLoginREPL {

    public PreLoginREPL() {
        repl();
    }

    private void repl() {
        System.out.println("♕ 240 Chess Client. Type help to get started: ");
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print(">>> ");
            try {
                result = ClientMain.evaluate(scanner.nextLine());
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }
        ClientMain.startREPL();
    }

    public static String helpText() {
        return """
                register <USERNAME> <PASSWORD> <EMAIL>
                login <USERNAME> <PASSWORD>
                quit
                help
                """;
    }

}

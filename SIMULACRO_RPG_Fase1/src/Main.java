import game.Game;
import io.ConsoleIO;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(new ConsoleIO(scanner));
        game.iniciar();
        scanner.close();
    }
}

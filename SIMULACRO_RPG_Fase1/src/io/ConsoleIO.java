package io;

import java.util.List;

/** Implementação para o modo terminal (Scanner + System.out). */
public class ConsoleIO implements GameIO {

    private final java.util.Scanner scanner;

    public ConsoleIO(java.util.Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }
}

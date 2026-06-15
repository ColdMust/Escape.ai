package puzzles;

import java.util.Scanner;

/** Identificar o operador correto em um loop while. */
public class OperadorWhilePuzzle implements Puzzle {

    @Override
    public String getId() { return "operador_while"; }

    @Override
    public String getTitulo() { return "Correção de Loop"; }

    @Override
    public String getLore() {
        return """
            >> SETOR: NÚCLEO DE ITERAÇÃO
            >> Um módulo de processamento entrou em loop infinito.
            >> O operador de comparação foi corrompido por um vírus.
            >> Restaure a condição de parada do while.""";
    }

    @Override
    public boolean executar(Scanner scanner) {
        System.out.println("Código corrompido:");
        System.out.println();
        System.out.println("  int x = 0;");
        System.out.println("  while (x ??? 10) {");
        System.out.println("      x++;");
        System.out.println("  }");
        System.out.println();
        System.out.println("Qual operador faz o loop executar EXATAMENTE 10 vezes?");
        System.out.println("  [1] >    [2] <    [3] ==    [4] >=");
        System.out.print("\nSua resposta: ");

        try {
            int resp = Integer.parseInt(scanner.nextLine().trim());
            if (resp == 2) {
                System.out.println("\n✅ CORRETO! O operador '<' faz x ir de 0 a 9 (10 iterações).");
                System.out.println("   Loop corrigido. Módulo restaurado.");
                return true;
            }
            System.out.println("\n❌ Incorreto. Tente novamente.");
            System.out.println("   Dica: o loop roda enquanto a condição for verdadeira.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
        return false;
    }
}

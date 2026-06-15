package puzzles;

import java.util.Scanner;

/** Completar a condição de um loop for. */
public class EstruturaForPuzzle implements Puzzle {

    @Override
    public String getId() { return "estrutura_for"; }

    @Override
    public String getTitulo() { return "Rotina do Daemon"; }

    @Override
    public String getLore() {
        return """
            >> SETOR: FILA DE PROCESSOS
            >> Um Processo Daemon precisa executar exatamente 5 ciclos e encerrar.
            >> A estrutura for foi parcialmente apagada por um worm.
            >> Complete a condição de repetição.""";
    }

    @Override
    public boolean executar(Scanner scanner) {
        System.out.println("Código incompleto:");
        System.out.println();
        System.out.println("  for (int i = 0; i ??? 5; i++) {");
        System.out.println("      executarCiclo(i);");
        System.out.println("  }");
        System.out.println();
        System.out.println("Qual operador faz o for executar EXATAMENTE 5 vezes (i = 0, 1, 2, 3, 4)?");
        System.out.println("  [1] >    [2] <    [3] <=    [4] >=");
        System.out.print("\nSua resposta: ");

        try {
            int resp = Integer.parseInt(scanner.nextLine().trim());
            if (resp == 2) {
                System.out.println("\n✅ CORRETO! 'i < 5' executa com i = 0, 1, 2, 3, 4 (5 iterações).");
                System.out.println("   Daemon encerrado com sucesso.");
                return true;
            }
            System.out.println("\n❌ Incorreto. Tente novamente.");
            System.out.println("   Dica: o loop continua enquanto a condição for verdadeira.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
        return false;
    }
}

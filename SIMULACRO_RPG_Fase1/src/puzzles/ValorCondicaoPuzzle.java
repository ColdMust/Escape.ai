package puzzles;

import java.util.Scanner;

/** Descobrir um valor válido que satisfaz uma condição composta. */
public class ValorCondicaoPuzzle implements Puzzle {

    @Override
    public String getId() { return "valor_condicao"; }

    @Override
    public String getTitulo() { return "Estabilização de Memória"; }

    @Override
    public String getLore() {
        return """
            >> SETOR: GERENCIADOR DE MEMÓRIA
            >> A alocação de RAM está instável — variáveis corrompidas.
            >> O sistema só permanece estável se a memória estiver no intervalo válido.
            >> Informe um valor que satisfaça a condição abaixo.""";
    }

    @Override
    public boolean executar(Scanner scanner) {
        System.out.println("Código do monitor de memória:");
        System.out.println();
        System.out.println("  int memoria = ???;");
        System.out.println("  if (memoria >= 0 && memoria <= 100) {");
        System.out.println("      sistemaEstavel = true;");
        System.out.println("  }");
        System.out.println();
        System.out.println("Digite um valor INTEIRO que mantenha o sistema estável (0 a 100):");
        System.out.print("\nSua resposta: ");

        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            if (valor >= 0 && valor <= 100) {
                System.out.println("\n✅ CORRETO! memoria = " + valor + " satisfaz 0 <= memoria <= 100.");
                System.out.println("   RAM estabilizada. Buffer restaurado.");
                return true;
            }
            System.out.println("\n❌ Valor fora do intervalo. O sistema travou novamente.");
            System.out.println("   Dica: a condição exige memoria >= 0 E memoria <= 100.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida — digite um número inteiro.");
        }
        return false;
    }
}

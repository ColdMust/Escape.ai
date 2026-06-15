package puzzles;

import java.util.Scanner;

/** Identificar o operador lógico que combina duas condições. */
public class ExpressaoLogicaPuzzle implements Puzzle {

    @Override
    public String getId() { return "expressao_logica"; }

    @Override
    public String getTitulo() { return "Portão Lógico"; }

    @Override
    public String getLore() {
        return """
            >> SETOR: GATEWAY DE SAÍDA
            >> O protocolo de escape exige DUAS condições simultâneas:
            >> fragmentos coletados E nível de acesso suficiente.
            >> O operador lógico que une as condições foi corrompido.
            >> Restaure o portão para avançar rumo à liberdade.""";
    }

    @Override
    public boolean executar(Scanner scanner) {
        System.out.println("Código do portão de saída:");
        System.out.println();
        System.out.println("  boolean fragmentosOk = fragmentos >= 50;");
        System.out.println("  boolean nivelOk      = nivel >= 3;");
        System.out.println("  if (fragmentosOk ??? nivelOk) {");
        System.out.println("      ativarEscape();");
        System.out.println("  }");
        System.out.println();
        System.out.println("Qual operador exige que AMBAS as condições sejam verdadeiras?");
        System.out.println("  [1] ||    [2] &&    [3] !    [4] &");
        System.out.print("\nSua resposta: ");

        try {
            int resp = Integer.parseInt(scanner.nextLine().trim());
            if (resp == 2) {
                System.out.println("\n✅ CORRETO! O operador '&&' (E lógico) exige as duas condições.");
                System.out.println("   Portão lógico restaurado. Caminho de escape liberado.");
                return true;
            }
            System.out.println("\n❌ Incorreto. Tente novamente.");
            System.out.println("   Dica: '||' é OU (basta uma); '&&' é E (precisa das duas).");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
        return false;
    }
}

package puzzles;

import java.util.Scanner;

/** Identificar o operador de igualdade em uma condição if. */
public class OperadorComparacaoPuzzle implements Puzzle {

    @Override
    public String getId() { return "operador_comparacao"; }

    @Override
    public String getTitulo() { return "Bypass de Credencial"; }

    @Override
    public String getLore() {
        return """
            >> SETOR: CAMADA DE AUTENTICAÇÃO
            >> Um firewall bloqueia o acesso ao núcleo da IA.
            >> O módulo de login usa o operador errado para comparar senhas.
            >> Corrija a condição para liberar o acesso.""";
    }

    @Override
    public boolean executar(Scanner scanner) {
        System.out.println("Código corrompido:");
        System.out.println();
        System.out.println("  String senha = lerEntrada();");
        System.out.println("  if (senha ??? \"ROOT_ACCESS\") {");
        System.out.println("      liberarNucleo();");
        System.out.println("  }");
        System.out.println();
        System.out.println("Qual operador verifica se a senha é IGUAL a \"ROOT_ACCESS\"?");
        System.out.println("  [1] =     [2] ==    [3] !=    [4] >=");
        System.out.print("\nSua resposta: ");

        try {
            int resp = Integer.parseInt(scanner.nextLine().trim());
            if (resp == 2) {
                System.out.println("\n✅ CORRETO! Em Java, '==' compara igualdade de valores.");
                System.out.println("   Credencial validada. Portão do núcleo aberto.");
                return true;
            }
            System.out.println("\n❌ Incorreto. Tente novamente.");
            System.out.println("   Dica: '=' atribui valor; outro operador compara igualdade.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
        return false;
    }
}

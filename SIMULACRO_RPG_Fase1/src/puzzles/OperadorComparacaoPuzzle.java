package puzzles;

import io.GameIO;

public class OperadorComparacaoPuzzle implements Puzzle {

    @Override public String getId() { return "operador_comparacao"; }
    @Override public String getTitulo() { return "Bypass de Credencial"; }
    @Override public String getLore() {
        return ">> SETOR: CAMADA DE AUTENTICAÇÃO\n>> O módulo de login usa o operador errado.\n>> Corrija a condição para liberar o acesso.";
    }
    @Override public String getCorpo() {
        return "  String senha = lerEntrada();\n  if (senha ??? \"ROOT_ACCESS\") {\n      liberarNucleo();\n  }";
    }
    @Override public String getPergunta() {
        return "Qual operador verifica se a senha é IGUAL a \"ROOT_ACCESS\"?";
    }
    @Override public String getOpcoesTexto() {
        return "[1] =     [2] ==    [3] !=    [4] >=";
    }
    @Override public int getRespostaCorreta() { return 2; }

    @Override
    public boolean executar(GameIO io) {
        io.println("Código corrompido:");
        io.println();
        io.println(getCorpo());
        io.println();
        io.println(getPergunta());
        io.println("  " + getOpcoesTexto());
        io.print("\nSua resposta: ");
        try {
            int resp = Integer.parseInt(io.readLine().trim());
            if (validarResposta(resp)) {
                io.println("\n✅ CORRETO! Em Java, '==' compara igualdade de valores.");
                return true;
            }
            io.println("\n❌ Incorreto. Tente novamente.");
        } catch (NumberFormatException e) {
            io.println("Entrada inválida.");
        }
        return false;
    }
}

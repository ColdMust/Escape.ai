package puzzles;

import io.GameIO;

public class OperadorWhilePuzzle implements Puzzle {

    @Override public String getId() { return "operador_while"; }
    @Override public String getTitulo() { return "Correção de Loop"; }
    @Override public String getLore() {
        return ">> SETOR: NÚCLEO DE ITERAÇÃO\n>> Um módulo entrou em loop infinito.\n>> Restaure a condição de parada do while.";
    }
    @Override public String getCorpo() {
        return "  int x = 0;\n  while (x ??? 10) {\n      x++;\n  }";
    }
    @Override public String getPergunta() {
        return "Qual operador faz o loop executar EXATAMENTE 10 vezes?";
    }
    @Override public String getOpcoesTexto() {
        return "[1] >    [2] <    [3] ==    [4] >=";
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
                io.println("\n✅ CORRETO! O operador '<' faz x ir de 0 a 9 (10 iterações).");
                return true;
            }
            io.println("\n❌ Incorreto. Tente novamente.");
        } catch (NumberFormatException e) {
            io.println("Entrada inválida.");
        }
        return false;
    }
}

package puzzles;

import io.GameIO;

public class EstruturaForPuzzle implements Puzzle {

    @Override public String getId() { return "estrutura_for"; }
    @Override public String getTitulo() { return "Rotina do Daemon"; }
    @Override public String getLore() {
        return ">> SETOR: FILA DE PROCESSOS\n>> Um Daemon precisa executar 5 ciclos.\n>> Complete a condição de repetição.";
    }
    @Override public String getCorpo() {
        return "  for (int i = 0; i ??? 5; i++) {\n      executarCiclo(i);\n  }";
    }
    @Override public String getPergunta() {
        return "Qual operador faz o for executar EXATAMENTE 5 vezes (i = 0..4)?";
    }
    @Override public String getOpcoesTexto() {
        return "[1] >    [2] <    [3] <=    [4] >=";
    }
    @Override public int getRespostaCorreta() { return 2; }

    @Override
    public boolean executar(GameIO io) {
        io.println("Código incompleto:");
        io.println();
        io.println(getCorpo());
        io.println();
        io.println(getPergunta());
        io.println("  " + getOpcoesTexto());
        io.print("\nSua resposta: ");
        try {
            int resp = Integer.parseInt(io.readLine().trim());
            if (validarResposta(resp)) {
                io.println("\n✅ CORRETO! 'i < 5' executa com i = 0, 1, 2, 3, 4.");
                return true;
            }
            io.println("\n❌ Incorreto. Tente novamente.");
        } catch (NumberFormatException e) {
            io.println("Entrada inválida.");
        }
        return false;
    }
}

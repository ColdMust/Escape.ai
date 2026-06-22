package puzzles;

import io.GameIO;

public class ExpressaoLogicaPuzzle implements Puzzle {

    @Override public String getId() { return "expressao_logica"; }
    @Override public String getTitulo() { return "Portão Lógico"; }
    @Override public String getLore() {
        return ">> SETOR: GATEWAY DE SAÍDA\n>> Duas condições devem ser verdadeiras.\n>> Restaure o operador lógico corrompido.";
    }
    @Override public String getCorpo() {
        return "  boolean fragmentosOk = fragmentos >= 50;\n  boolean nivelOk = nivel >= 3;\n  if (fragmentosOk ??? nivelOk) {\n      ativarEscape();\n  }";
    }
    @Override public String getPergunta() {
        return "Qual operador exige que AMBAS as condições sejam verdadeiras?";
    }
    @Override public String getOpcoesTexto() {
        return "[1] ||    [2] &&    [3] !    [4] &";
    }
    @Override public int getRespostaCorreta() { return 2; }

    @Override
    public boolean executar(GameIO io) {
        io.println("Código do portão de saída:");
        io.println();
        io.println(getCorpo());
        io.println();
        io.println(getPergunta());
        io.println("  " + getOpcoesTexto());
        io.print("\nSua resposta: ");
        try {
            int resp = Integer.parseInt(io.readLine().trim());
            if (validarResposta(resp)) {
                io.println("\n✅ CORRETO! O operador '&&' exige as duas condições.");
                return true;
            }
            io.println("\n❌ Incorreto. Tente novamente.");
        } catch (NumberFormatException e) {
            io.println("Entrada inválida.");
        }
        return false;
    }
}

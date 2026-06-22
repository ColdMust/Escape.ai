package puzzles;

import io.GameIO;

public class ValorCondicaoPuzzle implements Puzzle {

    @Override public String getId() { return "valor_condicao"; }
    @Override public String getTitulo() { return "Estabilização de Memória"; }
    @Override public String getLore() {
        return ">> SETOR: GERENCIADOR DE MEMÓRIA\n>> A RAM está instável.\n>> Informe um valor que satisfaça a condição.";
    }
    @Override public String getCorpo() {
        return "  int memoria = ???;\n  if (memoria >= 0 && memoria <= 100) {\n      sistemaEstavel = true;\n  }";
    }
    @Override public String getPergunta() {
        return "Digite um valor INTEIRO que mantenha o sistema estável (0 a 100):";
    }
    @Override public String getOpcoesTexto() {
        return "(digite um número de 0 a 100)";
    }
    @Override public int getRespostaCorreta() { return -1; }

    @Override
    public boolean validarResposta(int resposta) {
        return resposta >= 0 && resposta <= 100;
    }

    @Override
    public boolean executar(GameIO io) {
        io.println("Código do monitor de memória:");
        io.println();
        io.println(getCorpo());
        io.println();
        io.println(getPergunta());
        io.print("\nSua resposta: ");
        try {
            int valor = Integer.parseInt(io.readLine().trim());
            if (validarResposta(valor)) {
                io.println("\n✅ CORRETO! memoria = " + valor + " satisfaz 0 <= memoria <= 100.");
                return true;
            }
            io.println("\n❌ Valor fora do intervalo.");
        } catch (NumberFormatException e) {
            io.println("Entrada inválida — digite um número inteiro.");
        }
        return false;
    }
}

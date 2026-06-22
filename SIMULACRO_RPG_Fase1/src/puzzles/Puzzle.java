package puzzles;

import io.GameIO;

public interface Puzzle {

    String getId();

    String getTitulo();

    String getLore();

    /** Texto do código corrompido exibido no desafio. */
    String getCorpo();

    String getPergunta();

    String getOpcoesTexto();

    int getRespostaCorreta();

    default boolean validarResposta(int resposta) {
        return resposta == getRespostaCorreta();
    }

    boolean executar(GameIO io);
}

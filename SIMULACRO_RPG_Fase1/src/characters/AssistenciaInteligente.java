package characters;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AssistenciaInteligente extends Personagem {

    private static final Random RNG = new Random();
    private boolean inicializando = true; // imune no turno em que foi gerada

    public AssistenciaInteligente(int vidaBase, int ataqueBase, int defesaBase) {
        super("Assistência Inteligente",
              (int)(vidaBase  * 0.25),        // 25% HP do jogador
              (int)(ataqueBase * 0.30),        // 30% ATK do jogador
              defesaBase);
    }

    /** toma +5% dano nos demais. */
    @Override
    public void receberDano(int dano) {
        if (inicializando) return;
        super.receberDano((int) Math.ceil(dano * 1.05));
    }

    /** Deve ser chamado pelo BattleSystem no início do próximo turno. */
    public void finalizarInicializacao() { inicializando = false; }
    public boolean isInicializando()     { return inicializando; }

    public String agirAutomaticamente(List<Inimigo> inimigos) {
        List<Inimigo> alvos = inimigos.stream()
                .filter(i -> !i.isControlado() && i.estaVivo())
                .collect(Collectors.toList());

        if (alvos.isEmpty()) return "  🤖 " + nome + " não encontrou alvos.";

        Inimigo alvo = alvos.get(RNG.nextInt(alvos.size()));

        if (RNG.nextBoolean()) {
            int dano = calcularDano(alvo);
            alvo.receberDano(dano);
            return "  🤖 " + nome + " atacou " + alvo.getNome() + " causando " + dano + " de dano!";
        } else {
            int dano = calcularDano(alvo) + 6;
            alvo.receberDano(dano);
            return "  🤖 " + nome + " HACKEOU " + alvo.getNome() + "! Dano: " + dano;
        }
    }
}

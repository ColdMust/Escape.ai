package characters;

import items.DropTable;
import items.Item;

public class Inimigo extends Personagem {

    private int xpRecompensa;
    private int fragmentosRecompensa;
    private String descricao;

    // ── Construtor ───────────────────────────────────────────────
    public Inimigo(String nome, int vida, int ataque, int defesa,
                   int xpRecompensa, int fragmentos, String descricao) {
        super(nome, vida, ataque, defesa);
        this.xpRecompensa         = xpRecompensa;
        this.fragmentosRecompensa = fragmentos;
        this.descricao            = descricao;
    }

    /**
     * Sorteia e retorna um único item de drop ao ser derrotado.
     * O resultado é determinado na hora da chamada (não pré-fixado),
     * garantindo que cada morte tenha sua própria rolagem independente.
     *
     * @return o Item sorteado, ou {@code null} se não houver drop.
     */
    public Item rolarDrop() {
        return DropTable.sortearDrop();
    }

    // ── Fábrica de inimigos ──────────────────────────────────────
    public static Inimigo criarFirewallCorrompido() {
        return new Inimigo(
            "Firewall Corrompido", 40, 10, 3,
            50, 10,
            "Um fragmento de firewall que foi infectado por código malicioso."
        );
    }

    public static Inimigo criarBugVoador() {
        return new Inimigo(
            "Bug Voador", 25, 12, 1,
            35, 8,
            "Erro de runtime que ganhou consciência e voa pelo sistema."
        );
    }

    public static Inimigo criarProcessoDaemon() {
        return new Inimigo(
            "Processo Daemon", 60, 10, 6,
            80, 20,
            "Processo em background corrompido que drena recursos do sistema."
        );
    }

    public static Inimigo criarVirusPolimorfico() {
        return new Inimigo(
            "Vírus Polimórfico", 35, 15, 2,
            65, 15,
            "Muda sua estrutura a cada turno para escapar de detecção."
        );
    }

    // ── Getters ──────────────────────────────────────────────────
    public int    getXpRecompensa()         { return xpRecompensa; }
    public int    getFragmentosRecompensa() { return fragmentosRecompensa; }
    public String getDescricao()            { return descricao; }
}
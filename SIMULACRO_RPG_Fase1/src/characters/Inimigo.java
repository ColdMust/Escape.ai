package characters;

import items.Item;

public class Inimigo extends Personagem {

    private int xpRecompensa;
    private int fragmentosRecompensa;
    private Item itemDrop;       // pode ser null
    private String descricao;   // lore do inimigo

    // ── Construtor ───────────────────────────────────────────────
    public Inimigo(String nome, int vida, int ataque, int defesa,
                   int xpRecompensa, int fragmentos, String descricao) {
        super(nome, vida, ataque, defesa);
        this.xpRecompensa        = xpRecompensa;
        this.fragmentosRecompensa = fragmentos;
        this.descricao           = descricao;
        this.itemDrop            = null;
    }

    public void setItemDrop(Item item) {
        this.itemDrop = item;
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
        Inimigo e = new Inimigo(
            "Bug Voador", 25, 12, 1,
            35, 8,
            "Erro de runtime que ganhou consciência e voa pelo sistema."
        );
        e.setItemDrop(items.Item.criarPatchExe());
        return e;
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
    public Item   getItemDrop()             { return itemDrop; }
    public String getDescricao()            { return descricao; }
}

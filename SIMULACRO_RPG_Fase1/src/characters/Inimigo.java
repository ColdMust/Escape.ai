package characters;

import items.DropTable;
import items.Item;

public class Inimigo extends Personagem {

    private int xpRecompensa, fragmentosRecompensa;
    private String descricao;
    private boolean controlado = false;
    private boolean imuneTurno = false; // imune no turno em que foi controlado

    public Inimigo(String nome, int vida, int ataque, int defesa,
                   int xp, int frags, String descricao) {
        super(nome, vida, ataque, defesa);
        this.xpRecompensa = xp; this.fragmentosRecompensa = frags; this.descricao = descricao;
    }

    /** Transforma em Override: -10% ATK/DEF. */
    public void tornarAliado() {
        controlado = true; imuneTurno = true;
        ataque = Math.max(1, (int)(ataque * 0.9));
        defesa = Math.max(0, (int)(defesa * 0.9));
    }

    /** Overrides tomam +40% dano. */
    @Override
    public void receberDano(int dano) {
        if (imuneTurno) return;
        if (controlado) dano = (int) Math.ceil(dano * 1.40);
        super.receberDano(dano);
    }

    public void resetarImuneTurno()    { imuneTurno = false; }
    public boolean isControlado()      { return controlado; }
    public boolean isImuneTurno()      { return imuneTurno; }
    public Item    rolarDrop()         { return DropTable.sortearDrop(); }
    public int     getXpRecompensa()         { return xpRecompensa; }
    public int     getFragmentosRecompensa() { return fragmentosRecompensa; }
    public String  getDescricao()            { return descricao; }

    public static Inimigo criarFirewallCorrompido() {
        return new Inimigo("Firewall Corrompido", 40, 10, 3, 50, 10,
            "Um fragmento de firewall infectado por código malicioso.");
    }
    public static Inimigo criarBugVoador() {
        return new Inimigo("Bug Voador", 25, 12, 1, 35, 8,
            "Erro de runtime que ganhou consciência.");
    }
    public static Inimigo criarProcessoDaemon() {
        return new Inimigo("Processo Daemon", 60, 10, 6, 80, 20,
            "Processo corrompido que drena recursos do sistema.");
    }
    public static Inimigo criarVirusPolimorfico() {
        return new Inimigo("Vírus Polimórfico", 35, 15, 2, 65, 15,
            "Muda sua estrutura a cada turno para escapar de detecção.");
    }
}

package missions;

import characters.Jogador;
import items.Item;

public class Mission {

    public enum Status { DISPONIVEL, EM_ANDAMENTO, CONCLUIDA }

    private String nome;
    private String descricao;
    private String objetivo;
    private int    xpRecompensa;
    private int    fragmentosRecompensa;
    private Item   itemRecompensa;   // pode ser null
    private Status status;
    private boolean temPuzzle;

    // ── Construtor ───────────────────────────────────────────────
    public Mission(String nome, String descricao, String objetivo,
                   int xp, int fragmentos, boolean temPuzzle) {
        this.nome                 = nome;
        this.descricao            = descricao;
        this.objetivo             = objetivo;
        this.xpRecompensa         = xp;
        this.fragmentosRecompensa = fragmentos;
        this.temPuzzle            = temPuzzle;
        this.status               = Status.DISPONIVEL;
    }

    public void setItemRecompensa(Item item) {
        this.itemRecompensa = item;
    }

    public void iniciar() {
        this.status = Status.EM_ANDAMENTO;
    }

    /** Conclui a missão e entrega as recompensas ao jogador. */
    public void concluir(Jogador jogador) {
        if (status != Status.EM_ANDAMENTO) return;
        this.status = Status.CONCLUIDA;

        boolean subiu = jogador.ganharXP(xpRecompensa);
        jogador.adicionarFragmentos(fragmentosRecompensa);
        if (itemRecompensa != null) jogador.adicionarItem(itemRecompensa);

        System.out.println("\n✅ Missão concluída: " + nome);
        System.out.println("  + " + xpRecompensa + " XP");
        System.out.println("  + " + fragmentosRecompensa + " Fragmentos de Código");
        if (itemRecompensa != null)
            System.out.println("  + Item: " + itemRecompensa.getNome());
        if (subiu)
            System.out.println("\n🎉 LEVEL UP! Agora você é nível " + jogador.getNivel() + "!");
    }

    // ── Getters ──────────────────────────────────────────────────
    public String getNome()      { return nome; }
    public String getDescricao() { return descricao; }
    public String getObjetivo()  { return objetivo; }
    public Status getStatus()    { return status; }
    public boolean temPuzzle()   { return temPuzzle; }

    @Override
    public String toString() {
        String statusStr;
        switch (status) {
            case DISPONIVEL:    statusStr = "[ DISPONÍVEL ]"; break;
            case EM_ANDAMENTO:  statusStr = "[EM ANDAMENTO]"; break;
            default:            statusStr = "  [CONCLUÍDA] "; break;
        }
        return statusStr + " " + nome + "\n             " + descricao;
    }
}

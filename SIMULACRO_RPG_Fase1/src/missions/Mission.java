package missions;

import characters.Jogador;
import items.Item;

public class Mission {

    public enum Status { DISPONIVEL, EM_ANDAMENTO, CONCLUIDA, BLOQUEADA }

    private final String nome;
    private final String descricao;
    private final String objetivo;
    private final int xpRecompensa;
    private final int fragmentosRecompensa;
    private final String puzzleId;
    private final int fase;
    private final String missaoRequerida;

    private Item itemRecompensa;
    private Status status;

    public Mission(String nome, String descricao, String objetivo,
                   int xp, int fragmentos, String puzzleId, int fase, String missaoRequerida) {
        this.nome                 = nome;
        this.descricao            = descricao;
        this.objetivo             = objetivo;
        this.xpRecompensa         = xp;
        this.fragmentosRecompensa = fragmentos;
        this.puzzleId             = puzzleId;
        this.fase                 = fase;
        this.missaoRequerida      = missaoRequerida;
        this.status               = Status.DISPONIVEL;
    }

    public void setItemRecompensa(Item item) {
        this.itemRecompensa = item;
    }

    public void iniciar() {
        if (status == Status.DISPONIVEL) {
            this.status = Status.EM_ANDAMENTO;
        }
    }

    public void desbloquear() {
        if (status == Status.BLOQUEADA) {
            this.status = Status.DISPONIVEL;
        }
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

    public String getNome()              { return nome; }
    public String getDescricao()         { return descricao; }
    public String getObjetivo()          { return objetivo; }
    public Status getStatus()            { return status; }
    public boolean temPuzzle()           { return puzzleId != null; }
    public String getPuzzleId()          { return puzzleId; }
    public int getFase()                 { return fase; }
    public String getMissaoRequerida()   { return missaoRequerida; }

    @Override
    public String toString() {
        String statusStr;
        switch (status) {
            case DISPONIVEL:    statusStr = "[ DISPONÍVEL ]"; break;
            case EM_ANDAMENTO:  statusStr = "[EM ANDAMENTO]"; break;
            case BLOQUEADA:     statusStr = " [ BLOQUEADA ]"; break;
            default:            statusStr = "  [CONCLUÍDA] "; break;
        }
        String faseStr = fase == 2 ? " [Fase 2]" : "";
        String puzzleStr = puzzleId != null ? " 🧩 PUZZLE" : "";
        return statusStr + faseStr + puzzleStr + " " + nome + "\n             " + descricao;
    }
}

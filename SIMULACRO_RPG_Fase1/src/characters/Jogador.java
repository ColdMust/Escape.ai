package characters;

import items.Item;
import java.util.ArrayList;

public class Jogador extends Personagem {

    public enum Classe {
        PROGRAMADOR ("Programador", 0, 0, 0,  "Ataque e defesa equilibrados."),
        HACKER      ("Hacker",      5, -2, 0, "Alto dano, pouca defesa."),
        ANALISTA    ("Analista",    -2, 5, 0,  "Defesa alta, suporte."),
        ENGENHEIRO  ("Engenheiro de IA", 2, 2, 10,"Habilidades especiais aprimoradas.");

        public final String nome;
        public final int bonusAtaque;
        public final int bonusDefesa;
        public final int bonusVida;
        public final String descricao;

        Classe(String nome, int ba, int bd, int bv, String desc) {
            this.nome = nome; this.bonusAtaque = ba;
            this.bonusDefesa = bd; this.bonusVida = bv;
            this.descricao = desc;
        }
    }

    private Classe classe;
    private int xp;
    private int nivel;
    private int xpParaProximoNivel;
    private ArrayList<Item> inventario;
    private int fragmentosDeCodigo;   // moeda do jogo

    // ── Construtor ───────────────────────────────────────────────
    public Jogador(String nome, Classe classe) {
        super(nome,
              100 + classe.bonusVida,
              10  + classe.bonusAtaque,
              5   + classe.bonusDefesa);
        this.classe              = classe;
        this.xp                  = 0;
        this.nivel               = 1;
        this.xpParaProximoNivel  = 100;
        this.inventario          = new ArrayList<>();
        this.fragmentosDeCodigo  = 0;
    }

    // ── XP / Level ───────────────────────────────────────────────
    public boolean ganharXP(int quantidade) {
        xp += quantidade;
        if (xp >= xpParaProximoNivel) {
            subirDeNivel();
            return true;   // subiu de nível
        }
        return false;
    }

    private void subirDeNivel() {
        nivel++;
        xp -= xpParaProximoNivel;
        xpParaProximoNivel = nivel * 100;

        // Crescimento de atributos por nível
        int ganhoVida    = 15;
        int ganhoAtaque  = 3;
        int ganhoDefesa  = 2;

        vidaMax += ganhoVida;
        ataque  += ganhoAtaque;
        defesa  += ganhoDefesa;
        vida     = vidaMax;   // cura total ao subir de nível
    }

    // ── Inventário ───────────────────────────────────────────────
    public void adicionarItem(Item item) {
        inventario.add(item);
    }

    public boolean usarItem(int indice) {
        if (indice < 0 || indice >= inventario.size()) return false;
        Item item = inventario.get(indice);
        item.aplicar(this);
        inventario.remove(indice);
        return true;
    }

    /** Habilidade especial de hackear — dano bônus baseado na classe */
    public int hackear(Personagem alvo) {
        int danoBase = calcularDano(alvo);
        int bonus;
        switch (classe) {
            case HACKER:     bonus = 10; break;
            case ENGENHEIRO: bonus = 8;  break;
            case PROGRAMADOR:bonus = 5;  break;
            default:         bonus = 3;
        }
        int danoTotal = danoBase + bonus;
        alvo.receberDano(danoTotal);
        return danoTotal;
    }

    public String barraXP() {
        int total  = 20;
        int cheios = (int) ((double) xp / xpParaProximoNivel * total);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < total; i++) sb.append(i < cheios ? "▓" : "░");
        sb.append("] ").append(xp).append("/").append(xpParaProximoNivel);
        return sb.toString();
    }

    // ── Getters ──────────────────────────────────────────────────
    public Classe          getClasse()              { return classe; }
    public int             getXP()                  { return xp; }
    public int             getNivel()               { return nivel; }
    public int             getXpParaProximoNivel()  { return xpParaProximoNivel; }
    public ArrayList<Item> getInventario()          { return inventario; }
    public int             getFragmentos()          { return fragmentosDeCodigo; }
    public void            adicionarFragmentos(int n){ fragmentosDeCodigo += n; }
}

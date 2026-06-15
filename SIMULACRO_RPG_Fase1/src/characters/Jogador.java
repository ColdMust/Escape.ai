package characters;

import items.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogador extends Personagem {

    public enum Classe {
        PROGRAMADOR("Programador",      0,  0,  0, "Buff passivo ATK/DEF (80%) a cada ação."),
        HACKER     ("Hacker",           5, -2,  0, "Debuff passivo no inimigo (60%) a cada ação."),
        ANALISTA   ("Analista",        -2,  5,  0, "Hackear pode controlar até 2 Overrides (20%)."),
        ENGENHEIRO ("Engenheiro de IA", 2,  2, 10, "30% de chance de gerar Assistência Inteligente por turno.");

        public final String nome, descricao;
        public final int bonusAtaque, bonusDefesa, bonusVida;
        Classe(String n, int ba, int bd, int bv, String d) {
            nome = n; bonusAtaque = ba; bonusDefesa = bd; bonusVida = bv; descricao = d;
        }
    }

    private static final Random RNG = new Random();
    private static final int BUFF_VALOR = 3, DEBUFF_VALOR = 3;

    private final Classe classe;
    private int xp, nivel, xpParaProximoNivel, fragmentosDeCodigo;
    private final ArrayList<Item> inventario = new ArrayList<>();

    /** Até 2 Overrides ativos (Analista). */
    private final List<Inimigo> overrides = new ArrayList<>();

    private AssistenciaInteligente assistencia = null;

    public Jogador(String nome, Classe classe) {
        super(nome, 100 + classe.bonusVida, 10 + classe.bonusAtaque, 5 + classe.bonusDefesa);
        this.classe = classe; this.xp = 0; this.nivel = 1; this.xpParaProximoNivel = 100;
    }

    // ── Passivas ─────────────────────────────────────────────────

    public String passivaProgramador() {
        if (classe != Classe.PROGRAMADOR || RNG.nextDouble() >= 0.80) return null;
        if (RNG.nextBoolean()) { aplicarBuffAtaque(BUFF_VALOR);
            return "✨ [PASSIVA] Buff ATK+" + BUFF_VALOR + " (efetivo: " + getAtaqueEfetivo() + ")"; }
        aplicarBuffDefesa(BUFF_VALOR);
        return "✨ [PASSIVA] Buff DEF+" + BUFF_VALOR + " (efetivo: " + getDefesaEfetiva() + ")";
    }

    public String passivaHacker(Personagem alvo) {
        if (classe != Classe.HACKER || alvo == null || RNG.nextDouble() >= 0.60) return null;
        if (RNG.nextBoolean()) { alvo.aplicarDebuffAtaque(DEBUFF_VALOR);
            return "🔻 [PASSIVA] Debuff ATK-" + DEBUFF_VALOR + " em " + alvo.getNome(); }
        alvo.aplicarDebuffDefesa(DEBUFF_VALOR);
        return "🔻 [PASSIVA] Debuff DEF-" + DEBUFF_VALOR + " em " + alvo.getNome();
    }

    /**
     * Analista: 20% de controlar alvoHackeado como Override.
     * só possível se restar ≥1 inimigo livre após o controle.
     */
    public String passivaAnalistaControle(List<Inimigo> inimigos, Inimigo alvoHackeado) {
        if (classe != Classe.ANALISTA) return null;

        long livres = inimigos.stream().filter(i -> i.estaVivo() && !i.isControlado()).count();
        // Precisa sobrar ao menos 1 inimigo livre após controlar este
        if (livres < 2) return null;
        // Limite de 2 overrides
        if (overrides.size() >= 2) return null;

        if (RNG.nextDouble() >= 0.20) return null;

        alvoHackeado.tornarAliado();
        overrides.add(alvoHackeado);
        return "🎯 [OVERRIDE] " + alvoHackeado.getNome() + " foi controlado! ("
                + overrides.size() + "/2 Overrides ativos)";
    }

    /** Buff 80% em TODOS os Overrides ativos. */
    public List<String> passivaBufOverrides() {
        List<String> msgs = new ArrayList<>();
        if (classe != Classe.ANALISTA) return msgs;
        for (Inimigo ov : overrides) {
            if (!ov.estaVivo()) continue;
            if (RNG.nextDouble() < 0.80) {
                if (RNG.nextBoolean()) { ov.aplicarBuffAtaque(BUFF_VALOR);
                    msgs.add("✨ [OVERRIDE] " + ov.getNome() + " ATK+" + BUFF_VALOR); }
                else { ov.aplicarBuffDefesa(BUFF_VALOR);
                    msgs.add("✨ [OVERRIDE] " + ov.getNome() + " DEF+" + BUFF_VALOR); }
            }
        }
        return msgs;
    }

    public AssistenciaInteligente passivaEngenheiro() {
        if (classe != Classe.ENGENHEIRO) return null;
        if (assistencia != null && assistencia.estaVivo()) return null;
        if (RNG.nextDouble() >= 0.30) return null;
        assistencia = new AssistenciaInteligente(vidaMax, ataque, defesa);
        return assistencia;
    }

    // ── XP ───────────────────────────────────────────────────────

    public boolean ganharXP(int qtd) {
        xp += qtd;
        if (xp >= xpParaProximoNivel) { subirDeNivel(); return true; }
        return false;
    }

    private void subirDeNivel() {
        nivel++; xp -= xpParaProximoNivel; xpParaProximoNivel = nivel * 100;
        vidaMax += 15; ataque += 3; defesa += 2; vida = vidaMax;
    }

    // ── Inventário ───────────────────────────────────────────────

    public void adicionarItem(Item item) { inventario.add(item); }

    public boolean usarItem(int i) {
        if (i < 0 || i >= inventario.size()) return false;
        inventario.get(i).aplicar(this); inventario.remove(i); return true;
    }

    public int hackear(Personagem alvo) {
        int bonus;
        switch (classe) {
            case HACKER: bonus = 10; break; case ENGENHEIRO: bonus = 8; break;
            case PROGRAMADOR: bonus = 5; break; default: bonus = 3;
        }
        int dano = calcularDano(alvo) + bonus;
        alvo.receberDano(dano); return dano;
    }

    public String barraXP() {
        int cheios = (int)((double) xp / xpParaProximoNivel * 20);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 20; i++) sb.append(i < cheios ? "▓" : "░");
        return sb.append("] ").append(xp).append("/").append(xpParaProximoNivel).toString();
    }

    // ── Getters ──────────────────────────────────────────────────

    public Classe              getClasse()             { return classe; }
    public int                 getXP()                 { return xp; }
    public int                 getNivel()              { return nivel; }
    public int                 getXpParaProximoNivel() { return xpParaProximoNivel; }
    public ArrayList<Item>     getInventario()         { return inventario; }
    public int                 getFragmentos()         { return fragmentosDeCodigo; }
    public void                adicionarFragmentos(int n) { fragmentosDeCodigo += n; }
    public List<Inimigo>       getOverrides()          { return overrides; }
    public void                removerOverride(Inimigo o) { overrides.remove(o); }
    public void                limparOverrides()       { overrides.clear(); }
    public AssistenciaInteligente getAssistencia()     { return assistencia; }
    public void                removerAssistencia()    { assistencia = null; }

    // Compatibilidade com código existente que use getAliadoControlado()
    public Inimigo getAliadoControlado() { return overrides.isEmpty() ? null : overrides.get(0); }
    public void    removerAliadoControlado() { if (!overrides.isEmpty()) overrides.remove(0); }
}


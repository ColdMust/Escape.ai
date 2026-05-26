package items;

import characters.Personagem;

public class Item {

    public enum Tipo {
        CURA, ATAQUE_BONUS, DEFESA_BONUS
    }

    private String nome;
    private String descricao;
    private int    valor;       // quantidade de cura ou bônus
    private Tipo   tipo;

    // ── Construtor ───────────────────────────────────────────────
    public Item(String nome, String descricao, int valor, Tipo tipo) {
        this.nome      = nome;
        this.descricao = descricao;
        this.valor     = valor;
        this.tipo      = tipo;
    }

    /** Aplica o efeito do item no personagem alvo */
    public void aplicar(Personagem alvo) {
        switch (tipo) {
            case CURA:
                alvo.curar(valor);
                break;
            case ATAQUE_BONUS:
                alvo.setAtaque(alvo.getAtaque() + valor);
                break;
            case DEFESA_BONUS:
                alvo.setDefesa(alvo.getDefesa() + valor);
                break;
        }
    }

    // ── Fábrica de itens ─────────────────────────────────────────
    public static Item criarPatchExe() {
        return new Item("Patch.exe", "Corrige bugs internos. Restaura 30 HP.", 30, Tipo.CURA);
    }

    public static Item criarHotfix() {
        return new Item("Hotfix v1.2", "Restaura 50 HP de emergência.", 50, Tipo.CURA);
    }

    public static Item criarOverclock() {
        return new Item("Overclock.dll", "Aumenta o ataque em +5 permanentemente.", 5, Tipo.ATAQUE_BONUS);
    }

    public static Item criarFirewall() {
        return new Item("Firewall Pessoal", "Aumenta a defesa em +4 permanentemente.", 4, Tipo.DEFESA_BONUS);
    }

    public static Item criarNanobot() {
        return new Item("Nanobot Médico", "Restaura 20 HP.", 20, Tipo.CURA);
    }

    // ── Getters ──────────────────────────────────────────────────
    public String getNome()      { return nome; }
    public String getDescricao() { return descricao; }
    public int    getValor()     { return valor; }
    public Tipo   getTipo()      { return tipo; }

    @Override
    public String toString() {
        return nome + " — " + descricao;
    }
}

package characters;

public abstract class Personagem {
    protected String nome;
    protected int vida;
    protected int vidaMax;
    protected int ataque;
    protected int defesa;
    protected boolean defendendo;

    // Buffs/Debuffs temporários (acumulativos por turno)
    protected int buffAtaque  = 0;
    protected int buffDefesa  = 0;
    protected int debuffAtaque = 0;
    protected int debuffDefesa = 0;

    public Personagem(String nome, int vida, int ataque, int defesa) {
        this.nome       = nome;
        this.vida       = vida;
        this.vidaMax    = vida;
        this.ataque     = ataque;
        this.defesa     = defesa;
        this.defendendo = false;
    }

    /**
     * Retorna o ataque efetivo considerando buff/debuff temporários.
     */
    public int getAtaqueEfetivo() {
        return Math.max(1, ataque + buffAtaque - debuffAtaque);
    }

    /**
     * Retorna a defesa efetiva considerando buff/debuff temporários.
     */
    public int getDefesaEfetiva() {
        return Math.max(0, defesa + buffDefesa - debuffDefesa);
    }

    /** Calcula dano causado a um alvo considerando buffs/debuffs. */
    public int calcularDano(Personagem alvo) {
        int defesaEfetiva = alvo.defendendo
                ? alvo.getDefesaEfetiva() * 2
                : alvo.getDefesaEfetiva();
        return Math.max(1, this.getAtaqueEfetivo() - defesaEfetiva);
    }

    public void receberDano(int dano) {
        this.vida = Math.max(0, this.vida - dano);
    }

    public void curar(int quantidade) {
        this.vida = Math.min(vidaMax, this.vida + quantidade);
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    // ── Buffs ────────────────────────────────────────────────────
    public void aplicarBuffAtaque(int valor) {
        buffAtaque += valor;
    }

    public void aplicarBuffDefesa(int valor) {
        buffDefesa += valor;
    }

    public void aplicarDebuffAtaque(int valor) {
        debuffAtaque += valor;
    }

    public void aplicarDebuffDefesa(int valor) {
        debuffDefesa += valor;
    }

    public int getBuffAtaque()   { return buffAtaque; }
    public int getBuffDefesa()   { return buffDefesa; }
    public int getDebuffAtaque() { return debuffAtaque; }
    public int getDebuffDefesa() { return debuffDefesa; }

    public String barraDeVida() {
        int total  = 20;
        int cheios = (int) ((double) vida / vidaMax * total);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < total; i++) sb.append(i < cheios ? "█" : "░");
        sb.append("] ").append(vida).append("/").append(vidaMax);
        return sb.toString();
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public String  getNome()      { return nome; }
    public int     getVida()      { return vida; }
    public int     getVidaMax()   { return vidaMax; }
    public int     getAtaque()    { return ataque; }
    public int     getDefesa()    { return defesa; }
    public boolean isDefendendo() { return defendendo; }
    public void    setDefendendo(boolean b) { this.defendendo = b; }
    public void    setAtaque(int a)  { this.ataque  = a; }
    public void    setDefesa(int d)  { this.defesa  = d; }
    public void    setVida(int v)    { this.vida    = Math.max(0, Math.min(vidaMax, v)); }
    public void    setVidaMax(int v) { this.vidaMax = v; this.vida = v; }
}

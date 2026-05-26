package characters;

public abstract class Personagem {
    protected String nome;
    protected int vida;
    protected int vidaMax;
    protected int ataque;
    protected int defesa;
    protected boolean defendendo;

    public Personagem(String nome, int vida, int ataque, int defesa) {
        this.nome    = nome;
        this.vida    = vida;
        this.vidaMax = vida;
        this.ataque  = ataque;
        this.defesa  = defesa;
        this.defendendo = false;
    }

    /** Calcula dano causado a um alvo: max(1, ataque - defesa_alvo) */
    public int calcularDano(Personagem alvo) {
        int defesaEfetiva = alvo.defendendo ? alvo.defesa * 2 : alvo.defesa;
        return Math.max(1, this.ataque - defesaEfetiva);
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

    public String barraDeVida() {
        int total  = 20;
        int cheios = (int) ((double) vida / vidaMax * total);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < total; i++) sb.append(i < cheios ? "█" : "░");
        sb.append("] ").append(vida).append("/").append(vidaMax);
        return sb.toString();
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public String getNome()      { return nome; }
    public int    getVida()      { return vida; }
    public int    getVidaMax()   { return vidaMax; }
    public int    getAtaque()    { return ataque; }
    public int    getDefesa()    { return defesa; }
    public boolean isDefendendo(){ return defendendo; }
    public void   setDefendendo(boolean b) { this.defendendo = b; }
    public void   setAtaque(int a)  { this.ataque  = a; }
    public void   setDefesa(int d)  { this.defesa  = d; }
    public void   setVidaMax(int v) { this.vidaMax = v; this.vida = v; }
}

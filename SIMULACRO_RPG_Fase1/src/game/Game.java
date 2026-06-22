package game;

import battle.BattleSystem;
import characters.Inimigo;
import characters.Jogador;
import io.GameIO;
import levels.LevelSystem;
import missions.MissionSystem;
import items.Item;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final GameIO io;
    private Jogador jogador;
    private BattleSystem batalha;
    private MissionSystem missoes;
    private boolean rodando;

    public Game(GameIO io) {
        this.io = io;
        this.rodando = true;
    }

    public String getHudText() {
        if (jogador == null) return "INICIALIZANDO...";
        return "SETOR-7 | Frag: " + jogador.getFragmentos() + " | Nv." + jogador.getNivel();
    }

    public Jogador getJogador() { return jogador; }

    public void iniciar() {
        exibirIntro();
        criarPersonagem();
        batalha = new BattleSystem(io);
        missoes = new MissionSystem(io);

        jogador.adicionarItem(Item.criarNanobot());
        jogador.adicionarItem(Item.criarPatchExe());

        io.println("\n[Pressione Enter para iniciar...]");
        tentarComandoSecreto(io.readLine().trim());

        while (rodando && jogador.estaVivo()) {
            exibirMenuPrincipal();
        }

        if (!jogador.estaVivo()) {
            io.println("\n💀 GAME OVER. O sistema foi corrompido.");
            io.println("   Reinicie para tentar novamente.");
        } else {
            io.println("\nAté a próxima sessão, " + jogador.getNome() + ".");
        }
    }

    private void exibirIntro() {
        io.println();
        io.println("╔══════════════════════════════════════════╗");
        io.println("║                                          ║");
        io.println("║              escape.ai                  ║");
        io.println("║         RPG dentro de uma IA             ║");
        io.println("║                                          ║");
        io.println("╚══════════════════════════════════════════╝");
        io.println();
        io.println(">> INICIALIZANDO AMBIENTE VIRTUAL...");
        io.println(">> CARGA DE MEMÓRIA: 98.7%");
        io.println(">> ANOMALIA DETECTADA NO SETOR-7");
        io.println(">> INSTÂNCIA DE CONSCIÊNCIA ATIVADA...");
        io.println();
        io.println("Você acorda dentro de uma IA.");
        io.println("Não se lembra de como chegou aqui.");
        io.println("Para escapar, você precisa hackear o sistema de dentro.");
        io.println();
    }

    private void criarPersonagem() {
        io.print("Qual é o seu nome, Operador? ");
        String nome = io.readLine().trim();
        if (nome.isEmpty()) nome = "Operador";

        io.println("\nEscolha sua classe:");
        io.println("  [1] Programador     — " + Jogador.Classe.PROGRAMADOR.descricao);
        io.println("  [2] Hacker           — " + Jogador.Classe.HACKER.descricao);
        io.println("  [3] Analista         — " + Jogador.Classe.ANALISTA.descricao);
        io.println("  [4] Engenheiro de IA — " + Jogador.Classe.ENGENHEIRO.descricao);
        io.print("\nSua escolha: ");

        Jogador.Classe classe;
        try {
            int op = Integer.parseInt(io.readLine().trim());
            switch (op) {
                case 2:  classe = Jogador.Classe.HACKER;     break;
                case 3:  classe = Jogador.Classe.ANALISTA;   break;
                case 4:  classe = Jogador.Classe.ENGENHEIRO; break;
                default: classe = Jogador.Classe.PROGRAMADOR;
            }
        } catch (NumberFormatException e) {
            classe = Jogador.Classe.PROGRAMADOR;
        }

        jogador = new Jogador(nome, classe);
        io.println("\n✅ Personagem criado: " + nome + " [" + classe.nome + "]");
    }

    private void exibirMenuPrincipal() {
        io.println("\n" + "═".repeat(40));
        io.println("  SETOR-7  |  Fragmentos: "
                + jogador.getFragmentos() + "  |  Nv." + jogador.getNivel());
        io.println("═".repeat(40));
        io.println("  [1] ⚔  Explorar e Batalhar");
        io.println("  [2] 📋  Missões");
        io.println("  [3] 👤  Ficha do Personagem");
        io.println("  [4] 🎒  Inventário");
        io.println("  [0] 🚪  Sair");
        io.println("═".repeat(40));
        io.print("  Escolha: ");

        String entrada = io.readLine().trim();
        if (tentarComandoSecreto(entrada)) return;

        try {
            int op = Integer.parseInt(entrada);
            switch (op) {
                case 1: explorar();                                          break;
                case 2: missoes.interagirComMissao(jogador);                break;
                case 3: LevelSystem.exibirFicha(jogador, io);               break;
                case 4: gerenciarInventario();                               break;
                case 0: rodando = false;                                     break;
                default: io.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            io.println("Entrada inválida.");
        }
    }

    /** Comando secreto: vipoli → batalha forçada contra o Vírus com hack imediato. */
    private boolean tentarComandoSecreto(String entrada) {
        if (!entrada.equalsIgnoreCase("vipoli")) return false;
        batalhaVipoli();
        return true;
    }

    private void batalhaVipoli() {
        io.println("\n>> Comando vipoli reconhecido...");
        io.println(">> Invocando Vírus Polimórfico — contra-hack iminente!");
        Inimigo virus = Inimigo.criarVirusPolimorfico();
        List<Inimigo> inimigos = new ArrayList<>();
        inimigos.add(virus);

        boolean venceu = batalha.iniciarBatalha(jogador, inimigos, true);

        if (venceu) {
            missoes.notificarVitoria(virus.getNome(), jogador);
            missions.Mission m = encontrarMissaoAtiva("Coleta de Fragmentos");
            if (m != null) missoes.verificarCondicaoConclusao(m, jogador);
        } else if (jogador.estaVivo()) {
            io.println("\n>> Recuperando instância com 30% de HP...");
            jogador.curar((int)(jogador.getVidaMax() * 0.3));
        }
    }

    private void explorar() {
        io.println("\n>> Você avança pelos corredores do sistema...");

        int qtd = BattleSystem.sortearQuantidadeInimigos(jogador);

        List<Inimigo> inimigos = new ArrayList<>();
        for (int i = 0; i < qtd; i++) inimigos.add(gerarInimigoAleatorio());

        if (qtd > 1)
            io.println(">> ALERTA: " + qtd + " ameaças detectadas simultaneamente!");

        boolean venceu = batalha.iniciarBatalha(jogador, inimigos);

        if (venceu) {
            missoes.notificarVitoria(inimigos.get(0).getNome(), jogador);
            missions.Mission m = encontrarMissaoAtiva("Coleta de Fragmentos");
            if (m != null) missoes.verificarCondicaoConclusao(m, jogador);
        } else {
            io.println("\n>> Recuperando instância com 30% de HP...");
            jogador.curar((int)(jogador.getVidaMax() * 0.3));
        }
    }

    private Inimigo gerarInimigoAleatorio() {
        switch ((int)(Math.random() * 4)) {
            case 0:  return Inimigo.criarFirewallCorrompido();
            case 1:  return Inimigo.criarBugVoador();
            case 2:  return Inimigo.criarProcessoDaemon();
            default: return Inimigo.criarVirusPolimorfico();
        }
    }

    private missions.Mission encontrarMissaoAtiva(String nome) {
        for (missions.Mission m : missoes.getMissoes()) {
            if (m.getNome().equals(nome)) return m;
        }
        return null;
    }

    private void gerenciarInventario() {
        if (jogador.getInventario().isEmpty()) {
            io.println("\n🎒 Inventário vazio.");
            return;
        }
        io.println("\n🎒 INVENTÁRIO:");
        for (int i = 0; i < jogador.getInventario().size(); i++) {
            io.println("  [" + (i + 1) + "] " + jogador.getInventario().get(i));
        }
        io.print("Usar qual item? (0 = voltar): ");
        String entrada = io.readLine().trim();
        if (tentarComandoSecreto(entrada)) return;
        try {
            int idx = Integer.parseInt(entrada) - 1;
            if (idx >= 0 && jogador.usarItem(idx)) {
                io.println("✅ Item usado com sucesso.");
            }
        } catch (NumberFormatException e) { /* voltar */ }
    }
}

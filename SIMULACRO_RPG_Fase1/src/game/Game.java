package game;

import battle.BattleSystem;
import characters.Inimigo;
import characters.Jogador;
import levels.LevelSystem;
import missions.MissionSystem;
import items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Jogador jogador;
    private BattleSystem batalha;
    private MissionSystem missoes;
    private Scanner scanner;
    private boolean rodando;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.rodando = true;
    }

    public void iniciar() {
        exibirIntro();
        criarPersonagem();
        batalha = new BattleSystem(scanner);
        missoes = new MissionSystem(scanner);

        jogador.adicionarItem(Item.criarNanobot());
        jogador.adicionarItem(Item.criarPatchExe());

        System.out.println("\n[Pressione Enter para iniciar...]");
        scanner.nextLine();

        while (rodando && jogador.estaVivo()) {
            exibirMenuPrincipal();
        }

        if (!jogador.estaVivo()) {
            System.out.println("\n💀 GAME OVER. O sistema foi corrompido.");
            System.out.println("   Reinicie para tentar novamente.");
        } else {
            System.out.println("\nAté a próxima sessão, " + jogador.getNome() + ".");
        }
        scanner.close();
    }

    private void exibirIntro() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║                                          ║");
        System.out.println("║         S I M U L A C R O . E X E       ║");
        System.out.println("║         RPG dentro de uma IA             ║");
        System.out.println("║                                          ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
        System.out.println(">> INICIALIZANDO AMBIENTE VIRTUAL...");
        System.out.println(">> CARGA DE MEMÓRIA: 98.7%");
        System.out.println(">> ANOMALIA DETECTADA NO SETOR-7");
        System.out.println(">> INSTÂNCIA DE CONSCIÊNCIA ATIVADA...");
        System.out.println();
        System.out.println("Você acorda dentro de uma IA.");
        System.out.println("Não se lembra de como chegou aqui.");
        System.out.println("Para escapar, você precisa hackear o sistema de dentro.");
        System.out.println();
    }

    private void criarPersonagem() {
        System.out.print("Qual é o seu nome, Operador? ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) nome = "Operador";

        System.out.println("\nEscolha sua classe:");
        System.out.println("  [1] Programador     — " + Jogador.Classe.PROGRAMADOR.descricao);
        System.out.println("  [2] Hacker           — " + Jogador.Classe.HACKER.descricao);
        System.out.println("  [3] Analista         — " + Jogador.Classe.ANALISTA.descricao);
        System.out.println("  [4] Engenheiro de IA — " + Jogador.Classe.ENGENHEIRO.descricao);
        System.out.print("\nSua escolha: ");

        Jogador.Classe classe;
        try {
            int op = Integer.parseInt(scanner.nextLine().trim());
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
        System.out.println("\n✅ Personagem criado: " + nome + " [" + classe.nome + "]");
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("  SETOR-7  |  Fragmentos: "
                + jogador.getFragmentos() + "  |  Nv." + jogador.getNivel());
        System.out.println("═".repeat(40));
        System.out.println("  [1] ⚔  Explorar e Batalhar");
        System.out.println("  [2] 📋  Missões");
        System.out.println("  [3] 👤  Ficha do Personagem");
        System.out.println("  [4] 🎒  Inventário");
        System.out.println("  [0] 🚪  Sair");
        System.out.println("═".repeat(40));
        System.out.print("  Escolha: ");

        try {
            int op = Integer.parseInt(scanner.nextLine().trim());
            switch (op) {
                case 1: explorar();                                          break;
                case 2: missoes.interagirComMissao(jogador);                break;
                case 3: LevelSystem.exibirFicha(jogador);                   break;
                case 4: gerenciarInventario();                               break;
                case 0: rodando = false;                                     break;
                default: System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    // ── Exploração ────────────────────────────────────────────────

    private void explorar() {
        System.out.println("\n>> Você avança pelos corredores do sistema...");

        int qtd = BattleSystem.sortearQuantidadeInimigos(jogador);

        // DEBUG — remover após confirmar funcionamento
        System.out.println(">> [DEBUG] Nível: " + jogador.getNivel()
                + " | Classe: " + jogador.getClasse().nome
                + " | Inimigos sorteados: " + qtd);

        List<Inimigo> inimigos = new ArrayList<>();
        for (int i = 0; i < qtd; i++) inimigos.add(gerarInimigoAleatorio());

        if (qtd > 1)
            System.out.println(">> ALERTA: " + qtd + " ameaças detectadas simultaneamente!");

        boolean venceu = batalha.iniciarBatalha(jogador, inimigos);

        if (venceu) {
            missoes.notificarVitoria(inimigos.get(0).getNome(), jogador);
            missoes.verificarCondicaoConclusao(
                encontrarMissaoAtiva("Coleta de Fragmentos"), jogador);
        } else {
            System.out.println("\n>> Recuperando instância com 30% de HP...");
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

    // ── Inventário ────────────────────────────────────────────────

    private void gerenciarInventario() {
        if (jogador.getInventario().isEmpty()) {
            System.out.println("\n🎒 Inventário vazio.");
            return;
        }
        System.out.println("\n🎒 INVENTÁRIO:");
        for (int i = 0; i < jogador.getInventario().size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + jogador.getInventario().get(i));
        }
        System.out.print("Usar qual item? (0 = voltar): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && jogador.usarItem(idx)) {
                System.out.println("✅ Item usado com sucesso.");
            }
        } catch (NumberFormatException e) { /* voltar */ }
    }
}

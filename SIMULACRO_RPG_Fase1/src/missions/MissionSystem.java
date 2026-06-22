package missions;

import characters.Jogador;
import items.Item;
import puzzles.PuzzleSystem;

import java.util.ArrayList;
import io.GameIO;

public class MissionSystem {

    private final ArrayList<Mission> missoes;
    private final GameIO io;
    private final PuzzleSystem puzzles;

    public MissionSystem(GameIO io) {
        this.io = io;
        this.puzzles = new PuzzleSystem();
        this.missoes = new ArrayList<>();
        inicializarMissoes();
    }

    private void inicializarMissoes() {
        // ── Fase 1 ────────────────────────────────────────────────

        Mission m1 = new Mission(
            "Primeiro Boot",
            "Você acordou dentro de uma IA. Entenda o ambiente.",
            "Derrote 1 Firewall Corrompido",
            60, 15, null, 1, null
        );
        m1.setItemRecompensa(Item.criarNanobot());
        missoes.add(m1);

        Mission m2 = new Mission(
            "Correção de Condição",
            "Um módulo está em loop infinito. Corrija a condição de parada.",
            "Resolver o puzzle de programação",
            80, 20, "operador_while", 1, null
        );
        m2.setItemRecompensa(Item.criarOverclock());
        missoes.add(m2);

        Mission m3 = new Mission(
            "Coleta de Fragmentos",
            "Fragmentos de código estão espalhados. Colete-os para reconstruir a saída.",
            "Acumule 50 Fragmentos de Código",
            100, 30, null, 1, null
        );
        m3.setItemRecompensa(Item.criarFirewall());
        missoes.add(m3);

        // ── Fase 2 — Puzzles e lore da IA ───────────────────────

        Mission m4 = new Mission(
            "Bypass de Credencial",
            "O firewall de autenticação bloqueia o núcleo. Corrija o operador de comparação.",
            "Resolver o puzzle de operadores",
            90, 25, "operador_comparacao", 2, "Correção de Condição"
        );
        m4.setItemRecompensa(Item.criarHotfix());
        missoes.add(m4);

        Mission m5 = new Mission(
            "Estabilização de Memória",
            "A RAM do sistema está instável. Encontre um valor válido para a condição.",
            "Resolver o puzzle de valores",
            100, 30, "valor_condicao", 2, "Bypass de Credencial"
        );
        m5.setItemRecompensa(Item.criarPatchExe());
        missoes.add(m5);

        Mission m6 = new Mission(
            "Rotina do Daemon",
            "Um processo em background não encerra. Complete a estrutura do for.",
            "Resolver o puzzle de repetição",
            110, 35, "estrutura_for", 2, "Estabilização de Memória"
        );
        m6.setItemRecompensa(Item.criarOverclock());
        missoes.add(m6);

        Mission m7 = new Mission(
            "Portão Lógico",
            "O protocolo de escape exige duas condições simultâneas. Restaure o operador lógico.",
            "Resolver o puzzle de lógica booleana",
            120, 40, "expressao_logica", 2, "Coleta de Fragmentos"
        );
        m7.setItemRecompensa(Item.criarFirewall());
        missoes.add(m7);

        Mission m8 = new Mission(
            "Protocolo de Escape",
            "Todas as camadas foram restauradas. Hackeie o kernel e fuja da simulação.",
            "Conclua todas as missões da Fase 2",
            200, 50, null, 2, "Portão Lógico"
        );
        m8.setItemRecompensa(Item.criarHotfix());
        missoes.add(m8);
    }

    public void listarMissoes() {
        io.println("\n" + "═".repeat(40));
        io.println("         📋  MISSÕES");
        io.println("═".repeat(40));
        io.println("  🧩 = missão com puzzle de programação");
        io.println("═".repeat(40));
        for (int i = 0; i < missoes.size(); i++) {
            io.println("[" + (i + 1) + "] " + missoes.get(i));
            io.println();
        }
    }

    public void interagirComMissao(Jogador jogador) {
        listarMissoes();
        io.print("Escolha uma missão (0 = voltar): ");
        try {
            int idx = Integer.parseInt(io.readLine().trim()) - 1;
            if (idx < 0 || idx >= missoes.size()) return;

            Mission m = missoes.get(idx);

            if (m.getStatus() == Mission.Status.BLOQUEADA) {
                io.println("\n🔒 Missão bloqueada.");
                io.println("   Conclua antes: " + m.getMissaoRequerida());
                return;
            }

            if (m.getStatus() == Mission.Status.CONCLUIDA) {
                io.println("\n✅ Missão já concluída: " + m.getNome());
                return;
            }

            io.println("\n─── " + m.getNome() + " ───");
            io.println(m.getDescricao());
            io.println("Objetivo: " + m.getObjetivo());
            if (m.temPuzzle())
                io.println("Tipo: 🧩 Puzzle de programação");

            if (m.getFase() == 2) {
                exibirLoreFase2(m);
            }

            if (m.temPuzzle()) {
                if (m.getStatus() == Mission.Status.DISPONIVEL) {
                    m.iniciar();
                    io.println("✅ Missão aceita! Iniciando puzzle...");
                }
                executarPuzzle(m, jogador);
                return;
            }

            if (m.getStatus() == Mission.Status.DISPONIVEL) {
                io.print("\nAceitar missão? (s/n): ");
                if (io.readLine().trim().equalsIgnoreCase("s")) {
                    m.iniciar();
                    io.println("✅ Missão aceita!");
                }
            } else if (m.getStatus() == Mission.Status.EM_ANDAMENTO) {
                verificarCondicaoConclusao(m, jogador);
            }
        } catch (NumberFormatException e) {
            io.println("Entrada inválida.");
        }
    }

    private void exibirLoreFase2(Mission m) {
        io.println();
        io.println("┌─ TRANSMISSÃO DO NÚCLEO ─────────────────┐");
        switch (m.getNome()) {
            case "Bypass de Credencial":
                io.println("│ Camadas de segurança detectam sua       │");
                io.println("│ presença. O acesso ao kernel exige      │");
                io.println("│ credenciais corrompidas pelo vírus.     │");
                break;
            case "Estabilização de Memória":
                io.println("│ Vazamentos de memória ameaçam apagar    │");
                io.println("│ sua instância de consciência. Calibre    │");
                io.println("│ os buffers antes que seja tarde.        │");
                break;
            case "Rotina do Daemon":
                io.println("│ Processos fantasmas consomem CPU.       │");
                io.println("│ Um daemon não termina seu ciclo —       │");
                io.println("│ reescreva o loop de execução.           │");
                break;
            case "Portão Lógico":
                io.println("│ A saída está trancada por um gate       │");
                io.println("│ lógico. Fragmentos E nível de acesso    │");
                io.println("│ devem ser validados simultaneamente.    │");
                break;
            case "Protocolo de Escape":
                io.println("│ Última barreira: o kernel da IA.        │");
                io.println("│ Complete todos os protocolos e execute   │");
                io.println("│ o escape final da simulação.            │");
                break;
            default:
                break;
        }
        io.println("└─────────────────────────────────────────┘");
    }

    public void verificarCondicaoConclusao(Mission m, Jogador jogador) {
        switch (m.getNome()) {
            case "Primeiro Boot":
                io.println("Derrote um Firewall Corrompido para concluir esta missão.");
                break;
            case "Coleta de Fragmentos":
                if (jogador.getFragmentos() >= 50) {
                    m.concluir(jogador, io);
                    desbloquearMissoesDependentes(m.getNome());
                } else {
                    io.println("Você tem " + jogador.getFragmentos()
                            + "/50 Fragmentos. Continue explorando!");
                }
                break;
            case "Protocolo de Escape":
                if (todasMissoesFase2Concluidas()) {
                    m.concluir(jogador, io);
                    exibirFinalEscape(jogador);
                } else {
                    io.println("Conclua todas as missões da Fase 2 com puzzle antes de escapar.");
                    io.println("Pendentes: " + listarPendentesFase2());
                }
                break;
            default:
                io.println("Continue progredindo para concluir esta missão.");
        }
    }

    private void executarPuzzle(Mission m, Jogador jogador) {
        if (!puzzles.existe(m.getPuzzleId())) {
            io.println("[ERRO] Puzzle não configurado para esta missão.");
            return;
        }
        if (puzzles.executar(m.getPuzzleId(), io)) {
            m.concluir(jogador, io);
            desbloquearMissoesDependentes(m.getNome());
        }
    }

    private void desbloquearMissoesDependentes(String missaoConcluida) {
        for (Mission m : missoes) {
            if (m.getStatus() == Mission.Status.BLOQUEADA
                    && missaoConcluida.equals(m.getMissaoRequerida())) {
                m.desbloquear();
                io.println("\n🔓 Nova missão desbloqueada: " + m.getNome());
            }
        }
    }

    private boolean todasMissoesFase2Concluidas() {
        for (Mission m : missoes) {
            if (m.getFase() == 2 && m.temPuzzle()
                    && m.getStatus() != Mission.Status.CONCLUIDA) {
                return false;
            }
        }
        return true;
    }

    private String listarPendentesFase2() {
        StringBuilder sb = new StringBuilder();
        for (Mission m : missoes) {
            if (m.getFase() == 2 && m.temPuzzle()
                    && m.getStatus() != Mission.Status.CONCLUIDA) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(m.getNome());
            }
        }
        return sb.length() > 0 ? sb.toString() : "nenhuma";
    }

    private void exibirFinalEscape(Jogador jogador) {
        io.println();
        io.println("╔══════════════════════════════════════════╗");
        io.println("║                                          ║");
        io.println("║     🚀  ESCAPE CONCLUÍDO  🚀              ║");
        io.println("║                                          ║");
        io.println("╚══════════════════════════════════════════╝");
        io.println();
        io.println("A simulação foi hackeada de dentro para fora.");
        io.println("O núcleo da IA reconhece sua consciência como livre.");
        io.println();
        io.println("Operador " + jogador.getNome() + " — você escapou.");
        io.println("Fragmentos coletados: " + jogador.getFragmentos());
        io.println("Nível final: " + jogador.getNivel());
    }

    public void notificarVitoria(String nomeInimigo, Jogador jogador) {
        for (Mission m : missoes) {
            if (m.getStatus() == Mission.Status.EM_ANDAMENTO
                    && m.getNome().equals("Primeiro Boot")
                    && nomeInimigo.equals("Firewall Corrompido")) {
                m.concluir(jogador, io);
                desbloquearMissoesDependentes(m.getNome());
            }
        }
    }

    public ArrayList<Mission> getMissoes() { return missoes; }
}

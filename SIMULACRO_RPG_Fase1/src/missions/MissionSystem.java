package missions;

import characters.Jogador;
import items.Item;
import java.util.ArrayList;
import java.util.Scanner;

public class MissionSystem {

    private ArrayList<Mission> missoes;
    private Scanner scanner;

    public MissionSystem(Scanner scanner) {
        this.scanner = scanner;
        this.missoes = new ArrayList<>();
        inicializarMissoes();
    }

    private void inicializarMissoes() {
        // Missão 1 – Sem puzzle
        Mission m1 = new Mission(
            "Primeiro Boot",
            "Você acordou dentro de uma IA. Entenda o ambiente.",
            "Derrote 1 Firewall Corrompido",
            60, 15, false
        );
        m1.setItemRecompensa(Item.criarNanobot());
        missoes.add(m1);

        // Missão 2 – Com puzzle de lógica
        Mission m2 = new Mission(
            "Correção de Condição",
            "Um módulo está em loop infinito. Corrija a condição de parada.",
            "Resolver o puzzle de programação",
            80, 20, true
        );
        m2.setItemRecompensa(Item.criarOverclock());
        missoes.add(m2);

        // Missão 3 – Sem puzzle
        Mission m3 = new Mission(
            "Coleta de Fragmentos",
            "Fragmentos de código estão espalhados. Colete-os para reconstruir a saída.",
            "Acumule 50 Fragmentos de Código",
            100, 30, false
        );
        m3.setItemRecompensa(Item.criarFirewall());
        missoes.add(m3);
    }

    // ── Exibe lista de missões ────────────────────────────────────
    public void listarMissoes() {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("         📋  MISSÕES");
        System.out.println("═".repeat(40));
        for (int i = 0; i < missoes.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + missoes.get(i));
            System.out.println();
        }
    }

    // ── Aceitar / interagir com missão ────────────────────────────
    public void interagirComMissao(Jogador jogador) {
        listarMissoes();
        System.out.print("Escolha uma missão (0 = voltar): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= missoes.size()) return;

            Mission m = missoes.get(idx);
            System.out.println("\n─── " + m.getNome() + " ───");
            System.out.println(m.getDescricao());
            System.out.println("Objetivo: " + m.getObjetivo());
            System.out.println("Status: " + m.getStatus());

            if (m.getStatus() == Mission.Status.DISPONIVEL) {
                System.out.print("\nAceitar missão? (s/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                    m.iniciar();
                    System.out.println("✅ Missão aceita!");
                }
            } else if (m.getStatus() == Mission.Status.EM_ANDAMENTO) {
                if (m.temPuzzle()) {
                    executarPuzzle(m, jogador);
                } else {
                    verificarCondicaoConclusao(m, jogador);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    // ── Verificação automática de missões sem puzzle ──────────────
    public void verificarCondicaoConclusao(Mission m, Jogador jogador) {
        switch (m.getNome()) {
            case "Primeiro Boot":
                // Concluída após primeira batalha vencida — verificada externamente
                System.out.println("Derrote um Firewall Corrompido para concluir esta missão.");
                break;
            case "Coleta de Fragmentos":
                if (jogador.getFragmentos() >= 50) {
                    m.concluir(jogador);
                } else {
                    System.out.println("Você tem " + jogador.getFragmentos()
                            + "/50 Fragmentos. Continue explorando!");
                }
                break;
            default:
                System.out.println("Continue progredindo para concluir esta missão.");
        }
    }

    // ── Puzzle de lógica de programação ──────────────────────────
    private void executarPuzzle(Mission m, Jogador jogador) {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("      🧩  PUZZLE DE PROGRAMAÇÃO");
        System.out.println("═".repeat(40));
        System.out.println("Um loop está preso. Analise o código:");
        System.out.println();
        System.out.println("  int x = 0;");
        System.out.println("  while (x ??? 10) {");
        System.out.println("      x++;");
        System.out.println("  }");
        System.out.println();
        System.out.println("Qual operador faz o loop executar EXATAMENTE 10 vezes?");
        System.out.println("  [1] >    [2] <    [3] ==    [4] >=");
        System.out.print("\nSua resposta: ");

        try {
            int resp = Integer.parseInt(scanner.nextLine().trim());
            if (resp == 2) {  // < é a resposta correta
                System.out.println("\n✅ CORRETO! O operador '<' faz x ir de 0 a 9 (10 iterações).");
                System.out.println("   Loop corrigido. Módulo restaurado.");
                m.concluir(jogador);
            } else {
                System.out.println("\n❌ Incorreto. Tente de novo mais tarde...");
                System.out.println("   Dica: o loop roda enquanto a condição for verdadeira.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    // ── Conclusão automática após batalha ────────────────────────
    public void notificarVitoria(String nomeInimigo, Jogador jogador) {
        for (Mission m : missoes) {
            if (m.getStatus() == Mission.Status.EM_ANDAMENTO
                    && m.getNome().equals("Primeiro Boot")
                    && nomeInimigo.equals("Firewall Corrompido")) {
                m.concluir(jogador);
            }
        }
    }

    public ArrayList<Mission> getMissoes() { return missoes; }
}

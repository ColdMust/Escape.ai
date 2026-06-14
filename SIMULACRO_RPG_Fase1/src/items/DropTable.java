package items;

import java.util.Random;

/**
 * Tabela de drop de itens consumíveis.
 *
 * Chances acumuladas (rolagem de 1–100):
 *   01–01  →  Overclock.dll      (1 %)   - extremamente raro
 *   02–02  →  Firewall Pessoal   (1 %)   - extremamente raro
 *   03–07  →  Hotfix v1.2        (5 %)   - muito raro
 *   08–17  →  Patch.exe         (10 %)   - raro
 *   18–32  →  Nanobot Médico    (15 %)   - comum
 *   33–100 →  Nenhum drop       (68 %)
 *
 * Total de chance de dropar algo: 32 %
 */
public class DropTable {

    // ── Thresholds acumulados (%) ────────────────────────────────
    private static final int CHANCE_OVERCLOCK  =  1;   // 01–01
    private static final int CHANCE_FIREWALL   =  2;   // 02–02
    private static final int CHANCE_HOTFIX     =  7;   // 03–07
    private static final int CHANCE_PATCH      = 17;   // 08–17
    private static final int CHANCE_NANOBOT    = 32;   // 18–32
    // 33–100 → sem drop

    private static final Random RNG = new Random();

    // ── API pública ──────────────────────────────────────────────

    /**
     * Sorteia um item conforme a tabela de drop.
     *
     * @return o Item sorteado, ou {@code null} se não houver drop.
     */
    public static Item sortearDrop() {
        int roll = RNG.nextInt(100) + 1; // 1 a 100 (inclusive)
        return resolverRoll(roll);
    }

    // ── Resolução ────────────────────────────────────────────────

    private static Item resolverRoll(int roll) {
        if (roll <= CHANCE_OVERCLOCK) return Item.criarOverclock();
        if (roll <= CHANCE_FIREWALL)  return Item.criarFirewall();
        if (roll <= CHANCE_HOTFIX)    return Item.criarHotfix();
        if (roll <= CHANCE_PATCH)     return Item.criarPatchExe();
        if (roll <= CHANCE_NANOBOT)   return Item.criarNanobot();
        return null; // sem drop
    }
}
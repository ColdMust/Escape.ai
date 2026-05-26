#!/bin/bash
# SIMULACRO.EXE — RPG em Java
# Compatível com Linux e macOS

echo ""
echo " ╔══════════════════════════════════════════╗"
echo " ║        SIMULACRO.EXE  — Iniciando        ║"
echo " ╚══════════════════════════════════════════╝"
echo ""

# ── Verifica javac ─────────────────────────────────────────────
if ! command -v javac &> /dev/null; then
    echo " [ERRO] javac não encontrado."
    echo ""
    if [[ "$OSTYPE" == "darwin"* ]]; then
        echo " No macOS, instale com:"
        echo "   brew install openjdk@21"
        echo " Ou baixe em: https://www.oracle.com/java/technologies/downloads/"
    else
        echo " No Ubuntu/Debian, instale com:"
        echo "   sudo apt install default-jdk"
        echo " No Fedora/RHEL:"
        echo "   sudo dnf install java-21-openjdk-devel"
    fi
    exit 1
fi

# ── Cria pasta de saída ────────────────────────────────────────
mkdir -p out

echo " Compilando fontes..."

javac -encoding UTF-8 -d out \
    src/characters/Personagem.java \
    src/characters/Jogador.java \
    src/characters/Inimigo.java \
    src/items/Item.java \
    src/battle/BattleSystem.java \
    src/missions/Mission.java \
    src/missions/MissionSystem.java \
    src/levels/LevelSystem.java \
    src/game/Game.java \
    src/Main.java

if [ $? -ne 0 ]; then
    echo ""
    echo " [ERRO] Falha na compilação. Verifique os arquivos em src/"
    exit 1
fi

echo " OK! Criando JAR e iniciando o jogo..."
echo ""

# ── Cria o JAR executável ──────────────────────────────────────
echo "Main-Class: Main" > manifest.txt
jar cfm SIMULACRO.jar manifest.txt -C out .
rm manifest.txt

# ── Executa ────────────────────────────────────────────────────
java -jar SIMULACRO.jar

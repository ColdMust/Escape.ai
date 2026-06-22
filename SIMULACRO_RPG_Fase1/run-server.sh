#!/bin/bash
# SIMULACRO.EXE — Servidor Web + Java

echo ""
echo " ╔══════════════════════════════════════════╗"
echo " ║   SIMULACRO.EXE — Servidor Web + Java    ║"
echo " ╚══════════════════════════════════════════╝"
echo ""

cd "$(dirname "$0")"

if ! command -v javac &> /dev/null; then
    echo " [ERRO] javac não encontrado. Instale o JDK."
    exit 1
fi

mkdir -p out

echo " Compilando fontes Java..."
javac -encoding UTF-8 -d out \
    src/io/UiState.java \
    src/io/GameIO.java \
    src/io/ConsoleIO.java \
    src/io/RemoteGameIO.java \
    src/characters/Personagem.java \
    src/characters/Jogador.java \
    src/characters/Inimigo.java \
    src/characters/AssistenciaInteligente.java \
    src/items/Item.java \
    src/items/DropTable.java \
    src/battle/HackEscapeSystem.java \
    src/battle/BattleSystem.java \
    src/puzzles/Puzzle.java \
    src/puzzles/PuzzleSystem.java \
    src/puzzles/OperadorWhilePuzzle.java \
    src/puzzles/OperadorComparacaoPuzzle.java \
    src/puzzles/ValorCondicaoPuzzle.java \
    src/puzzles/EstruturaForPuzzle.java \
    src/puzzles/ExpressaoLogicaPuzzle.java \
    src/missions/Mission.java \
    src/missions/MissionSystem.java \
    src/levels/LevelSystem.java \
    src/game/Game.java \
    src/server/GameSession.java \
    src/server/GameServer.java \
    src/server/ServerMain.java

if [ $? -ne 0 ]; then
    echo " [ERRO] Falha na compilação."
    exit 1
fi

echo " OK! Iniciando servidor em http://localhost:9090"
echo ""

if command -v xdg-open &> /dev/null; then
    xdg-open "http://localhost:9090" 2>/dev/null &
elif [[ "$OSTYPE" == "darwin"* ]]; then
    open "http://localhost:9090" 2>/dev/null &
fi

java -cp out server.ServerMain

@echo off
chcp 65001 >nul
title SIMULACRO.EXE — Servidor Web + Java

echo.
echo  ╔══════════════════════════════════════════╗
echo  ║   SIMULACRO.EXE — Servidor Web + Java    ║
echo  ╚══════════════════════════════════════════╝
echo.

cd /d "%~dp0"

REM ── Verifica JDK ──────────────────────────────────────────────
call "%~dp0setup-java.bat"
if %errorlevel% neq 0 (
    echo  [ERRO] javac nao encontrado.
    echo.
    echo  Instale o JDK com:
    echo    winget install Microsoft.OpenJDK.21
    echo.
    echo  Se ja instalou, feche este terminal e execute novamente.
    pause
    exit /b 1
)

if not exist "out" mkdir out

echo  Compilando fontes Java...
javac -encoding UTF-8 -d out ^
    src\io\UiState.java ^
    src\io\GameIO.java ^
    src\io\ConsoleIO.java ^
    src\io\RemoteGameIO.java ^
    src\characters\Personagem.java ^
    src\characters\Jogador.java ^
    src\characters\Inimigo.java ^
    src\characters\AssistenciaInteligente.java ^
    src\items\Item.java ^
    src\items\DropTable.java ^
    src\battle\HackEscapeSystem.java ^
    src\battle\BattleSystem.java ^
    src\puzzles\Puzzle.java ^
    src\puzzles\PuzzleSystem.java ^
    src\puzzles\OperadorWhilePuzzle.java ^
    src\puzzles\OperadorComparacaoPuzzle.java ^
    src\puzzles\ValorCondicaoPuzzle.java ^
    src\puzzles\EstruturaForPuzzle.java ^
    src\puzzles\ExpressaoLogicaPuzzle.java ^
    src\missions\Mission.java ^
    src\missions\MissionSystem.java ^
    src\levels\LevelSystem.java ^
    src\game\Game.java ^
    src\server\GameSession.java ^
    src\server\GameServer.java ^
    src\server\ServerMain.java

if %errorlevel% neq 0 (
    echo  [ERRO] Falha na compilacao.
    pause
    exit /b 1
)

echo  OK! Iniciando servidor...
echo.
start http://localhost:9090
java -cp out server.ServerMain

pause

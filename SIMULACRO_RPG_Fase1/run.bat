@echo off
chcp 65001 >nul
title SIMULACRO.EXE — RPG em Java

echo.
echo  ╔══════════════════════════════════════════╗
echo  ║        SIMULACRO.EXE  — Iniciando        ║
echo  ╚══════════════════════════════════════════╝
echo.

REM ── Verifica se o Java está instalado ─────────────────────────
where javac >nul 2>&1
if %errorlevel% neq 0 (
    echo  [ERRO] javac nao encontrado.
    echo  Instale o JDK em: https://www.oracle.com/java/technologies/downloads/
    echo  Ou use: winget install Microsoft.OpenJDK.21
    pause
    exit /b 1
)

REM ── Cria pasta de saída ────────────────────────────────────────
if not exist "out" mkdir out

echo  Compilando fontes...

javac -encoding UTF-8 -d out ^
    src\characters\Personagem.java ^
    src\characters\Jogador.java ^
    src\characters\Inimigo.java ^
    src\items\Item.java ^
    src\items\DropTable.java ^
    src\battle\BattleSystem.java ^
    src\missions\Mission.java ^
    src\missions\MissionSystem.java ^
    src\levels\LevelSystem.java ^
    src\game\Game.java ^
    src\Main.java

if %errorlevel% neq 0 (
    echo.
    echo  [ERRO] Falha na compilacao. Verifique os arquivos em src\
    pause
    exit /b 1
)

echo  OK! Iniciando o jogo...
echo.

REM ── Cria o JAR executável ──────────────────────────────────────
echo Main-Class: Main > manifest.txt
jar cfm SIMULACRO.jar manifest.txt -C out .
del manifest.txt

REM ── Executa ────────────────────────────────────────────────────
java -jar SIMULACRO.jar

pause
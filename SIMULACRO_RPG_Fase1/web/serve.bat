@echo off
chcp 65001 >nul
title SIMULACRO.EXE — Servidor Web

echo.
echo  ╔══════════════════════════════════════════╗
echo  ║   SIMULACRO.EXE — Abrindo no navegador   ║
echo  ╚══════════════════════════════════════════╝
echo.

cd /d "%~dp0"

REM Tenta Python primeiro
where python >nul 2>&1
if %errorlevel% equ 0 (
    echo  Servidor: http://localhost:8080
    echo  Pressione Ctrl+C para encerrar.
    echo.
    start http://localhost:8080
    python -m http.server 8080
    exit /b 0
)

REM Fallback: Node npx serve
where npx >nul 2>&1
if %errorlevel% equ 0 (
    echo  Servidor: http://localhost:8080
    echo  Pressione Ctrl+C para encerrar.
    echo.
    start http://localhost:8080
    npx --yes serve . -l 8080
    exit /b 0
)

echo  [ERRO] Instale Python ou Node.js para rodar o servidor local.
echo  Python: winget install Python.Python.3.12
echo  Node:   winget install OpenJS.NodeJS.LTS
pause
exit /b 1

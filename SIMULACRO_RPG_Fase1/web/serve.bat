@echo off
chcp 65001 >nul
title SIMULACRO.EXE — Servidor Web

echo.
echo  ╔══════════════════════════════════════════╗
echo  ║   SIMULACRO.EXE — Modo Web (Java+JS)     ║
echo  ╚══════════════════════════════════════════╝
echo.

cd /d "%~dp0\.."
call run-server.bat

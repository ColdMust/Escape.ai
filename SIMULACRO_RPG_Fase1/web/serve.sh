#!/bin/bash
# SIMULACRO.EXE — Servidor Web local

cd "$(dirname "$0")"

echo ""
echo " ╔══════════════════════════════════════════╗"
echo " ║   SIMULACRO.EXE — Abrindo no navegador   ║"
echo " ╚══════════════════════════════════════════╝"
echo ""
echo " Servidor: http://localhost:8080"
echo " Pressione Ctrl+C para encerrar."
echo ""

if command -v python3 &> /dev/null; then
    (sleep 1 && xdg-open http://localhost:8080 2>/dev/null || open http://localhost:8080 2>/dev/null) &
    python3 -m http.server 8080
elif command -v python &> /dev/null; then
    (sleep 1 && xdg-open http://localhost:8080 2>/dev/null || open http://localhost:8080 2>/dev/null) &
    python -m http.server 8080
elif command -v npx &> /dev/null; then
    (sleep 1 && xdg-open http://localhost:8080 2>/dev/null || open http://localhost:8080 2>/dev/null) &
    npx --yes serve . -l 8080
else
    echo " [ERRO] Instale Python ou Node.js."
    exit 1
fi

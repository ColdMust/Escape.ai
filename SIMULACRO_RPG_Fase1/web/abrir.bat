@echo off
chcp 65001 >nul
echo.
echo  O modo web agora usa o servidor Java para a logica do jogo.
echo  Use run-server.bat ou web\serve.bat em vez de abrir index.html direto.
echo.
pause
start "" "%~dp0serve.bat"

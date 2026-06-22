@echo off
REM Detecta JDK instalado e adiciona ao PATH desta sessao.
REM Necessario quando o JDK foi instalado mas o terminal ainda nao foi reiniciado.

where javac >nul 2>&1
if %errorlevel% equ 0 exit /b 0

for /d %%D in ("C:\Program Files\Microsoft\jdk-*") do (
    set "PATH=%%~D\bin;%PATH%"
    goto :found
)

for /d %%D in ("C:\Program Files\Java\jdk-*") do (
    set "PATH=%%~D\bin;%PATH%"
    goto :found
)

for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-*") do (
    set "PATH=%%~D\bin;%PATH%"
    goto :found
)

exit /b 1

:found
where javac >nul 2>&1
if %errorlevel% equ 0 exit /b 0
exit /b 1

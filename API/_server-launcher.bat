@ECHO OFF
@setlocal enableextensions
@cd /d "%~dp0"

echo Lancement de la creation des trois bases de donnees si elles n'existent pas
echo - - - - - - - - - - - - - - - - - -

	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_development_fid";
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_test_fid";
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_production_fid";

echo - - - - - - - - - - - - - - - - - -
IF %ERRORLEVEL% NEQ 0 GOTO ProcessErrorMysql
echo Les trois bases de donnees existent bien sur le serveur mysql

echo.
echo.

echo Ouverture d'un nouveau cmd pour l'installation des dependances npm et lancement du serveur

	START cmd /K "npm install && npm start"
	GOTO end


:ProcessErrorMysql
echo Erreur mysql : la commande mysql n'est pas reconnue ou aucun serveur mysql n'est lance, fin du processus
pause
exit /b 0 

:end
pause
exit /b 1

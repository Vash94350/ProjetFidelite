#!/bin/bash

echo Lancement de la creation des trois bases de donnees si elles n\'existent pas
echo - - - - - - - - - - - - - - - - - -

	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_development_fid;"
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_test_fid;"
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_production_fid;"

echo - - - - - - - - - - - - - - - - - -

if [ $ -ne "0" ]
then
	echo Erreur mysql : la commande mysql n\'est pas reconnue ou aucun serveur mysql n\'est lance, fin du processus
	read a
	exit 0
fi

echo Les trois bases de donnees existent bien sur le serveur mysql

echo $'\n\n'

echo Ouverture d\'un nouveau cmd pour l\'installation des dependances npm et lancement du serveur

gnome-terminal -x bash -c "npm install; npm start; exec bash"

read a
exit 1

#!/bin/bash
echo Lancement de la creation des trois bases de donnees si elles n\'existent pas
echo - - - - - - - - - - - - - - - - - -
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_development_fid;"
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_test_fid;"
	mysql -u root -e "CREATE DATABASE IF NOT EXISTS database_production_fid;"
echo - - - - - - - - - - - - - - - - - -
if [ $? -eq 0 ]; then
    echo OK
else
    echo FAIL
fi
#!/bin/bash

echo Checking if our 3 databases already exist ...

echo Creating databases if not  
echo $'\n\n'
echo - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

mysql -e "CREATE DATABASE IF NOT EXISTS database_development_fid;
CREATE DATABASE IF NOT EXISTS database_test_fid;
CREATE DATABASE IF NOT EXISTS database_production_fid;
show databases" -u root -p

echo - - - - - - - - - - - - - - - - - - - - - - - - - - - -

echo $?

if [[ $? != 0 ]]
then
	echo mysql Error : wrong password, or mysql command not found or mysql server not launched ...
	echo end of the process ...
	read a
	exit 0
fi 

echo The 3 database are created on the mysql server !

echo $'\n\n'

echo Opening a new terminal for node.js installation and start ...

echo $'\n\n'

open -a Terminal _install_run_API.sh

echo Everything should be good now :D

read a
exit 1

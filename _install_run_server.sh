#!/bin/bash

pushd . > /dev/null
SCRIPT_PATH="${BASH_SOURCE[0]}";
  while([ -h "${SCRIPT_PATH}" ]) do 
    cd "`dirname "${SCRIPT_PATH}"`"
    SCRIPT_PATH="$(readlink "`basename "${SCRIPT_PATH}"`")"; 
  done
cd "`dirname "${SCRIPT_PATH}"`" > /dev/null
SCRIPT_PATH="`pwd`";
popd  > /dev/null

echo "${SCRIPT_PATH}"

cd "$SCRIPT_PATH"/API/

echo Installation of node modules if it is the first launch
echo $'\n\n'

npm install

echo $'\n\n'
echo Server is starting ...
echo $'\n\n'

npm start

echo $'\n\n'
echo Server is off now
echo Bye.
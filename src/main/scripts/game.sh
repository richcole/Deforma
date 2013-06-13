#!/bin/bash
homedir=$(dirname $0)

cd $homedir
homedir=$(pwd)
java -Djava.library.path=$homedir/natives -jar $homedir/Game-0.0.1.jar

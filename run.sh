#!/bin/bash

set -e -x

mvn package 
rm -rf tmp
mkdir tmp
cd tmp
unzip ../target/Game-0.0.1-release.zip
Game-0.0.1/game.sh


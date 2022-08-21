#!/usr/bin/env bash
set -euo pipefail

PASTDIR="$PWD"
DIR="$(realpath "$(dirname "${BASH_SOURCE[0]}")")"
cd "$DIR"/..
mkdir -p bin/ || rm -rf bin/*
mkdir -p docs/ || rm -rf docs/*

export CLASSPATH=lib/commons-cli-1.5.0.jar

printf "Compiling source code...\n"
find src -type f -name "*.java" | xargs javac -d bin/

printf "PASSED\nCompiling javadocs...\n"
find src -type f -name "*.java" | xargs javadoc -d docs/

printf "PASSED\nCompressing jarfile...\n"
cd bin
# This needs to change if the main entry method
# manifest.txt MUST point to the entry point of the program
printf "Main-Class: EzASM.Main\nClass-Path: ../lib/commons-cli-1.5.0.jar\n" > manifest.txt
jar cvfm out.jar manifest.txt EzASM/* ../lib/commons-cli-1.5.0.jar
printf "PASSED\n"

cd "$PASTDIR"
# to execute the program after compile, uncomment the following line
# exec runLinux.sh "$@"
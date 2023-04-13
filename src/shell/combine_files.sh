#!/usr/bin/env sh
# Combines the two input files and places the resulting files at the given output.
# This is used to create an "executable jar" file on systems with a posix-compliant shell.
# This file will be able to be both run as an executable and a jar interchangeably.
# This is possible since the information about the zip file formatting is stored in the footer so we can thus store a
# shell script in the header which will detect the Java installation and version and either execute the program or
# exit with an error message pertaining to the system's Java installation (does not exist, wrong version, etc.)

cat "$1" "$2" > "$3"
chmod +x "$3"

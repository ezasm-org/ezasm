#!/usr/bin/env bash
set -euo pipefail

DIR="$(realpath "$(dirname "${BASH_SOURCE[0]}")")/../bin"

# old execution
# java -classpath "PATH"/../bin: main/java/Main "$@"
# ; instead of : on Windows
java -jar "$DIR/out.jar" "$@"

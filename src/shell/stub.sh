#!/usr/bin/env sh
# This program is not to be used as a standalone, rather concatenated with the jar file to produce a unix executable.

ARGS="$@"

SELF="$(which "$0" 2>/dev/null)"
[ $? -gt  0 ] && test -f "$0" && SELF="./$0"

if which java >/dev/null 2>&1; then
    JAVA_EXECUTABLE_CMD=java
elif test -f "$JAVA_HOME/bin/java";  then
    JAVA_EXECUTABLE_CMD="$JAVA_HOME/bin/java"
else
    echo "Could not find Java on the system"
    exit 1
fi

VERSION="$(java -version 2>&1 | grep -oP 'version "?(1\.)?\K\d+')"
if [ "$VERSION" = "" ]; then
  echo "Could not find Java version"
  exit 1
elif [ "$VERSION" -lt "17" ]; then
  echo "Java version is less than 17, please use Java 17"
  exit 1
fi

exec "$JAVA_EXECUTABLE_CMD" -jar "$SELF" "$ARGS"

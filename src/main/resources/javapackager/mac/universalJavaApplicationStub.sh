#!/bin/sh
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

VERSION="$($JAVA_EXECUTABLE_CMD -version 2>&1 | head -1 | cut -d '"' -f 2 | sed -e 's/^1\.//' | cut -d '.' -f 1)"
if [ -z "$VERSION" ]; then
  echo "Could not find Java version"
  exit 1
elif [ "$VERSION" -lt "17" ]; then
  echo "System Java version $VERSION is less than 17, please use Java version 17 or higher"
  exit 1
fi

exec "$JAVA_EXECUTABLE_CMD" -jar "$SELF" $ARGS

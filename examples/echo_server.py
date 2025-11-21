#!/usr/bin/env python3
"""
Simple echo server for testing EzASM process pipes.
Reads from stdin, processes input, and writes to stdout.
"""

import sys

def main():
    # Disable buffering for immediate I/O
    sys.stdout = open(sys.stdout.fileno(), 'w', buffering=1)
    sys.stdin = open(sys.stdin.fileno(), 'r', buffering=1)
    
    print("Echo server started", file=sys.stderr)
    
    while True:
        try:
            # Read line from stdin
            line = sys.stdin.readline()
            if not line:
                break
            
            # Echo back with prefix
            response = f"ECHO: {line}"
            sys.stdout.write(response)
            sys.stdout.flush()
            
        except (EOFError, KeyboardInterrupt):
            break
    
    print("Echo server stopped", file=sys.stderr)

if __name__ == "__main__":
    main()

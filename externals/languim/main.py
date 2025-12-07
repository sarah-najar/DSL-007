#!/usr/bin/env python3
"""
LanguIM CLI Tool for ArduinoML
Parses LanguIM DSL files and generates Arduino wiring code
"""

import sys
from pathlib import Path
from languim_parser import parse_languim
from languim_codegen import generate_wiring_code


def main():
    if len(sys.argv) < 2:
        print("Usage: python main.py <languim_file>")
        print("Example: python main.py examples/toggle.lim")
        sys.exit(1)

    file_path = sys.argv[1]

    try:
        # Parse the LanguIM file
        model = parse_languim(file_path)
        print(f"✓ Successfully parsed: {file_path}", file=sys.stderr)
        print(f"✓ Application: {model.app_name}", file=sys.stderr)
        print(f"✓ Bricks: {len(model.bricks)}", file=sys.stderr)
        print(f"✓ States: {len(model.states)}", file=sys.stderr)

        # Generate code
        code = generate_wiring_code(model)
        print(code)

    except FileNotFoundError:
        print(f"Error: File not found: {file_path}", file=sys.stderr)
        sys.exit(1)
    except ValueError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"Unexpected error: {e}", file=sys.stderr)
        sys.exit(1)


if __name__ == "__main__":
    main()

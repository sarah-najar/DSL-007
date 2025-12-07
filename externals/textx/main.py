# TextX_external_DSL
# With temporal + buzzer extension
import sys
from pathlib import Path

from wiring_gen import load_model, to_wiring


def main():
    if len(sys.argv) < 2:
        raise SystemExit("Usage: python textx/main.py <path-to-model>")
    model_path = Path(sys.argv[1]).resolve()
    model = load_model(str(model_path))
    print(to_wiring(model))


if __name__ == "__main__":
    main()

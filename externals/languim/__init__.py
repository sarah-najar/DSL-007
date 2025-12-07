"""
LanguIM - A modern DSL for ArduinoML
Provides parsing and code generation for Arduino state machine applications
"""

__version__ = "1.0"
__all__ = ["parse_languim", "generate_wiring_code", "ArduinoMLModel"]

from .languim_parser import parse_languim, ArduinoMLModel
from .languim_codegen import generate_wiring_code

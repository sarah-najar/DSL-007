buzzer "bz" pin 8

state "short_beep" means "bz" shortBeep
state "long_beep" means "bz" longBeep

initial "short_beep"

from "short_beep" to "long_beep" after 800
from "long_beep" to "short_beep" after 1000

export "beeps"


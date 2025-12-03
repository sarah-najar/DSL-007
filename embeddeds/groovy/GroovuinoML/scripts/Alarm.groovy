sensor "button" pin 9
actuator "led" pin 12
buzzer "bz" pin 8

state "idle" means "led" becomes low
state "alarm" means "led" becomes high and "bz" shortBeep

initial "idle"

from "idle" to "alarm" when "button" becomes high
from "alarm" to "idle" after 500

export "alarm"


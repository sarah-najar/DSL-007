actuator "led" pin 12

state "on" means "led" becomes high
state "off" means "led" becomes low

initial "on"

from "on" to "off" after 500
from "off" to "on" after 1, s

export "timer"


potentiometer "pot" on 0
actuator "led" pin 12

state "low" means "led" becomes low
state "high" means "led" becomes high

initial "low"

from "low" to "high" whenPotAtLeast "pot", 600
from "high" to "low" whenPotBelow "pot", 600

export "pot_only"


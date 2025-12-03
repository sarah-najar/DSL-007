sensor "b1" pin 10
actuator "led" pin 12

state "waiting" means "led" becomes low
state "on" means "led" becomes high

initial "waiting"

from "waiting" to "on" when "b1" becomes high
from "on" to "waiting" after 800

export "temporal_after_push"


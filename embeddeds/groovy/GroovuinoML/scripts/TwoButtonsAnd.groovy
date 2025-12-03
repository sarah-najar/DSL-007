sensor "b1" pin 2
sensor "b2" pin 3
actuator "led" pin 12

state "idle" means "led" becomes low
state "on" means "led" becomes high

initial "idle"

from "idle" to "on" whenBothHigh "b1", "b2"
from "on" to "idle" when "b1" becomes low
from "on" to "idle" when "b2" becomes low

export "two_and"


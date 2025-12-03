sensor "button" pin 9
actuator "led" pin 12
buzzer "bz" pin 8

state "Quiet" means "bz" becomes low and "led" becomes low
state "Buzz" means "bz" becomes high and "led" becomes low
state "Light" means "bz" becomes low and "led" becomes high

initial "Quiet"

from "Quiet" to "Buzz" when "button" becomes high
from "Buzz" to "Light" when "button" becomes high
from "Light" to "Quiet" when "button" becomes high

export "multi"

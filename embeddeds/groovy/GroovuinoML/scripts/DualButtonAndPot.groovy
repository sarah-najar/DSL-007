def TH = 512

sensor "b1" pin 12
potentiometer "pot" on 0
buzzer "bz" pin 8

state "idle" means "bz" becomes low
state "alarm" means "bz" becomes high

initial "idle"

from "idle" to "alarm" whenButtonAndPotAtLeast "b1", "pot", TH
from "alarm" to "idle" whenEitherButtonLowOrPotBelow "b1", "pot", TH

export "dual"


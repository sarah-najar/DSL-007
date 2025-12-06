__author__ = 'arduino-ml'

import SIGNAL


class Transition:
    pass


class ConditionalTransition(Transition):
    def __init__(self, condition, nextstate):
        self.condition = condition
        self.nextstate = nextstate

    def condition_expr(self):
        return self.condition.to_arduino()

    def next_state_name(self):
        return self.nextstate.name


class TemporalTransition(Transition):
    def __init__(self, duration_ms, nextstate):
        self.duration_ms = int(duration_ms)
        self.nextstate = nextstate

    def condition_expr(self):
        return "millis() - time >= %d" % self.duration_ms

    def next_state_name(self):
        return self.nextstate.name

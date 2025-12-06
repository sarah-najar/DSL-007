__author__ = 'arduino-ml'

import SIGNAL


class Condition:
    def to_arduino(self):
        return "true"


class SensorLevelCondition(Condition):
    def __init__(self, sensor, expected):
        self.sensor = sensor
        self.expected = expected

    def to_arduino(self):
        name = self.sensor.name if hasattr(self.sensor, 'name') else str(self.sensor)
        value = SIGNAL.value(self.expected) if self.expected in (SIGNAL.HIGH, SIGNAL.LOW) or isinstance(self.expected, int) else str(self.expected)
        if value not in ("HIGH", "LOW"):
            # expected may be provided as string 'HIGH'/'LOW'
            value = str(self.expected)
        return "digitalRead(%s) == %s" % (name, value)


class AnalogThresholdCondition(Condition):
    def __init__(self, sensor, threshold, at_least):
        self.sensor = sensor
        self.threshold = int(threshold)
        self.at_least = bool(at_least)

    def to_arduino(self):
        name = self.sensor.name if hasattr(self.sensor, 'name') else str(self.sensor)
        op = ">=" if self.at_least else "<"
        return "analogRead(%s) %s %d" % (name, op, self.threshold)


class BinaryCondition(Condition):
    def __init__(self, left, right):
        self.left = left
        self.right = right


class AndCondition(BinaryCondition):
    def to_arduino(self):
        return "(%s) && (%s)" % (self.left.to_arduino(), self.right.to_arduino())


class OrCondition(BinaryCondition):
    def to_arduino(self):
        return "(%s) || (%s)" % (self.left.to_arduino(), self.right.to_arduino())


class NotCondition(Condition):
    def __init__(self, condition):
        self.condition = condition

    def to_arduino(self):
        return "!(%s)" % self.condition.to_arduino()


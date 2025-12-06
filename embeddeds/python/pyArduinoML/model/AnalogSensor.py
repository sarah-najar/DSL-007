__author__ = 'arduino-ml'

from pyArduinoML.model.Brick import Brick


class AnalogSensor(Brick):
    """
    An analog sensor (e.g., potentiometer). Pin is the analog index (A0..A5 => 0..5).
    """

    def __init__(self, name, analog_index):
        Brick.__init__(self, name, analog_index)

    def declare(self):
        return "int %s = A%d;" % (self.name, self.pin)

    def setup(self):
        return "\t// analog sensor %s on A%d" % (self.name, self.pin)


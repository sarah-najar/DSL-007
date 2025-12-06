__author__ = 'arduino-ml'

from pyArduinoML.model.Actuator import Actuator


class LCDScreen(Actuator):
    def __init__(self, name, columns, rows):
        Actuator.__init__(self, name, 0)
        self.columns = int(columns)
        self.rows = int(rows)

    def declare(self):
        # Basic placeholder; a real mapping of pins would be needed in practice
        return "// LCD %s (%dx%d)" % (self.name, self.columns, self.rows)

    def setup(self):
        return "\t// init LCD %s" % self.name


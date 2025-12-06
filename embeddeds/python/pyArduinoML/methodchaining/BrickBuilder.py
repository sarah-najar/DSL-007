__author__ = 'pascalpoizat'

from pyArduinoML.model.Actuator import Actuator
from pyArduinoML.model.Sensor import Sensor
from pyArduinoML.model.Buzzer import Buzzer
from pyArduinoML.model.LCDScreen import LCDScreen
from pyArduinoML.model.AnalogSensor import AnalogSensor

ACTUATOR = 0
SENSOR = 1
BUZZER = 2
LCD = 3
ANALOG_SENSOR = 4


class BrickBuilder:
    """
    Builder for bricks (MethodChaining)
    """

    def __init__(self, root, name, kind):
        """
        Constructor.

        :param root: AppBuilder, root builder
        :param name: Int, name of the brick
        :param kind: kind of brick to build
        :return:
        """
        self.root = root
        self.name = name
        self.kind = kind
        self.pin = None  # Int, pin of the brick (or analog index for analog sensors)
        self.lcd_cols = None
        self.lcd_rows = None

    def on_pin(self, pin):
        """
        Sets the pin of the brick

        :param pin: Int, pin of the brick
        :return: AppBuilder, builder of the whole application
        """
        self.pin = pin
        return self.root

    # dedicated for analog sensors (A0..A5 -> 0..5)
    def on(self, analog_index):
        self.pin = analog_index
        return self.root

    # dedicated for LCD screens
    def size(self, cols, rows):
        self.lcd_cols = int(cols)
        self.lcd_rows = int(rows)
        return self.root

    def get_contents(self):
        """
        Builds the brick.

        :return: Brick, the brick
        """
        if self.kind == ACTUATOR:
            return Actuator(self.name, self.pin)
        if self.kind == SENSOR:
            return Sensor(self.name, self.pin)
        if self.kind == BUZZER:
            return Buzzer(self.name, self.pin)
        if self.kind == LCD:
            return LCDScreen(self.name, self.lcd_cols or 16, self.lcd_rows or 2)
        if self.kind == ANALOG_SENSOR:
            return AnalogSensor(self.name, self.pin)
        return None

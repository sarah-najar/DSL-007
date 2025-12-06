__author__ = 'arduino-ml'


class LCDAction:
    def __init__(self, lcd, message):
        self.lcd = lcd
        self.message = message

    def to_arduino(self):
        return "\t// LCD %s display\n\t// lcd.print(\"%s\");" % (self.lcd.name, self.message.replace("\"", "\"\""))


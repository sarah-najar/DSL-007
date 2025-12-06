__author__ = 'arduino-ml'


class BuzzerAction:
    def __init__(self, buzzer, length):
        self.buzzer = buzzer
        self.length = length  # 'SHORT' or 'LONG'

    def to_arduino(self):
        dur = 100 if self.length == 'SHORT' else 500
        return "\tdigitalWrite(%s, HIGH);\n\tdelay(%d);\n\tdigitalWrite(%s, LOW);" % (self.buzzer.name, dur, self.buzzer.name)


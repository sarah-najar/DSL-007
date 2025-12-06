__author__ = 'pascalpoizat'

from pyArduinoML.model.Action import Action
from pyArduinoML.model.BuzzerAction import BuzzerAction
from pyArduinoML.model.LCDAction import LCDAction
from pyArduinoML.methodchaining.UndefinedBrick import UndefinedBrick


class StateActionBuilder:
    """
    Builder for actions in states (MethodChaining)
    """

    def __init__(self, root, actuator):
        """
        Constructor.

        :param root: StateBuilder, root builder
        :param actuator: String, name of the actuator to operate on
        :return:
        """
        self.root = root
        self.actuator = actuator
        self.data = None  # SIGNAL, signal to send to the actuator or other payload
        self.kind = 'actuator'  # 'actuator' | 'buzzer' | 'lcd'

    def to(self, data):
        """
        Sets the signal to send.

        :param data: SIGNAL, signal to send
        :return: BehaviorBuilder, builder for the behavior
        """
        self.data = data
        return self.root

    # DSL helpers for buzzer
    def shortBeep(self):
        self.kind = 'buzzer'
        self.data = 'SHORT'
        return self.root

    def longBeep(self):
        self.kind = 'buzzer'
        self.data = 'LONG'
        return self.root

    # DSL helper for lcd
    def display(self, message):
        self.kind = 'lcd'
        self.data = str(message)
        return self.root

    def get_contents(self, bricks):
        """
        Builds the action.

        :param bricks: Map[String,Brick] the bricks of the application
        :return: Action, the action to build
        :raises: UndefinedBrick, if the action references an undefined brick
        """
        if self.actuator not in bricks.keys():
            raise UndefinedBrick()
        target = bricks[self.actuator]
        if self.kind == 'buzzer':
            return BuzzerAction(target, self.data)
        if self.kind == 'lcd':
            return LCDAction(target, self.data)
        return Action(self.data, target)

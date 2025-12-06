__author__ = 'pascalpoizat'

from pyArduinoML.model.Transition import Transition
from pyArduinoML.model.Transition import ConditionalTransition, TemporalTransition
from pyArduinoML.model.conditions import SensorLevelCondition, AndCondition, OrCondition, AnalogThresholdCondition
from pyArduinoML.methodchaining.UndefinedBrick import UndefinedBrick
from pyArduinoML.methodchaining.UndefinedState import UndefinedState


class TransitionBuilder:
    """
    Builder for transitions.
    """

    def __init__(self, root, sensor=None):
        """
        Constructor.

        :param root: BehaviorBuilder, root builder
        :param sensor: String, name of the brick used to trigger the transition
        :return:
        """
        self.root = root
        self.sensor = sensor
        self.value = None  # SIGNAL, state of the brick to trigger the transition
        self.next_state = None  # String, name of the target state
        self.condition = None  # Condition for conditional transitions
        self.delay_ms = None   # int for temporal

    def has_value(self, value):
        """
        Sets the action.

        :param value: SIGNAL, state of the brick to trigger the transition
        :return: TransitionBuilder, the builder
        """
        self.value = value
        return self

    def becomes(self, value):
        return self.has_value(value)

    def go_to_state(self, next_state):
        """
        Sets the target state.

        :param next_state: String, name of the target state
        :return: StateBuilder, the builder root
        """
        self.next_state = next_state
        return self.root.root

    def get_contents(self, bricks, states):
        """
        Builds the transition.

        :param bricks: Map[String,Brick], the bricks of the application
        :param states: Map[String,State], the states of the application
        :return: Transition, the transition to build
        :raises: UndefinedBrick, if the transition references an undefined brick
        :raises: UndefinedState, if the transition references an undefined state
        """
        if self.delay_ms is not None:
            # temporal transition
            if self.next_state not in states.keys():
                raise UndefinedState()
            return TemporalTransition(self.delay_ms, states[self.next_state])
        # conditional transitions
        if self.condition is None:
            if self.sensor not in bricks.keys():
                raise UndefinedBrick()
            cond = SensorLevelCondition(bricks[self.sensor], self.value)
        else:
            cond = self.condition
        if self.next_state not in states.keys():
            raise UndefinedState()
        return ConditionalTransition(cond, states[self.next_state])

    # helpers to build composite conditions
    def both_high(self, s1, s2):
        self.condition = AndCondition(SensorLevelCondition(s1, 'HIGH'), SensorLevelCondition(s2, 'HIGH'))
        return self

    def pot_at_least(self, pot, th):
        self.condition = AnalogThresholdCondition(pot, th, True)
        return self

    def pot_below(self, pot, th):
        self.condition = AnalogThresholdCondition(pot, th, False)
        return self

    # temporal
    def after(self, amount, unit=None):
        ms = int(amount) * (1000 if (unit == 's' or unit == 'S') else 1)
        self.delay_ms = ms
        return self

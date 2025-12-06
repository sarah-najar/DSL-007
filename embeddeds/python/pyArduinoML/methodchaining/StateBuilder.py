__author__ = 'pascalpoizat'

from pyArduinoML.model.State import State
from pyArduinoML.model.Transition import Transition
from pyArduinoML.methodchaining.TransitionBuilder import TransitionBuilder
from pyArduinoML.methodchaining.StateActionBuilder import StateActionBuilder
from pyArduinoML.methodchaining.UndefinedBrick import UndefinedBrick
from pyArduinoML.methodchaining.UndefinedState import UndefinedState

class StateBuilder:
    """
    Builder for states.
    """

    def __init__(self, root, state):
        """
        Constructor.

        :param root: BehaviorBuilder, root builder
        :param state: String, state name
        :return:
        """
        self.root = root
        self.state = state
        self.actions = []  # List[StateActionBuilder]
        self.transitions = []  # List[TransitionBuilder]

    def set(self, actuator):
        """
        Adds an action to the state.

        :param actuator: String, brink the action operates on
        :return: StateActionBuilder, the builder for the action
        """
        action = StateActionBuilder(self, actuator)
        self.actions = self.actions + [action]
        return action

    def when(self, sensor):
        """
        Sets the transition of the state (unique in the current meta-model)

        :param sensor: String, brick to operate on
        :return: TransitionBuilder, the builder for the transition
        """
        transition = TransitionBuilder(self, sensor)
        self.transitions.append(transition)
        return transition

    def after(self, amount, unit=None):
        t = TransitionBuilder(self, None)
        t.after(amount, unit)
        self.transitions.append(t)
        return t

    # convenience helpers
    def whenBothHigh(self, s1, s2):
        t = TransitionBuilder(self, None)
        t.both_high(s1, s2)
        self.transitions.append(t)
        return t

    def whenPotAtLeast(self, pot, th):
        t = TransitionBuilder(self, None)
        t.pot_at_least(pot, th)
        self.transitions.append(t)
        return t

    def whenPotBelow(self, pot, th):
        t = TransitionBuilder(self, None)
        t.pot_below(pot, th)
        self.transitions.append(t)
        return t

    def whenButtonAndPotAtLeast(self, button, pot, th):
        t = TransitionBuilder(self, None)
        from pyArduinoML.model.conditions import AndCondition, SensorLevelCondition, AnalogThresholdCondition
        t.condition = AndCondition(SensorLevelCondition(button, 'HIGH'), AnalogThresholdCondition(pot, th, True))
        self.transitions.append(t)
        return t

    def whenEitherButtonLowOrPotBelow(self, button, pot, th):
        t = TransitionBuilder(self, None)
        from pyArduinoML.model.conditions import OrCondition, SensorLevelCondition, AnalogThresholdCondition
        t.condition = OrCondition(SensorLevelCondition(button, 'LOW'), AnalogThresholdCondition(pot, th, False))
        self.transitions.append(t)
        return t

    def get_contents(self, bricks):
        """
        Builds the state (step 1)

        :param bricks: Map[String, Brick], the bricks of the application
        :return: State, the state to build

        This method does not builds the transition attribute.
        A 2-step build is required (due to the meta-model) to get references right while avoiding bad typing tricks
        such as passing a TransitionBuilder instead of a Transition.
        """
        return State(self.state, map(lambda builder: builder.get_contents(bricks), self.actions), [])

    def get_contents2(self, bricks, states):
        """
         Builds the state (step 2)

        :param bricks: Map[String,Brick], the bricks of the application
        :param states: Map[String, State], the states of the application
        :return:
        :raises: UndefinedBrick, if the brick the transition operates on is not defined
        :raises: UndefinedState, if the target state is not defined

        This method builds the transition attribute.
        A 2-step build is required (due to the meta-model) to get references right while avoiding bad typing tricks
        such as passing a TransitionBuilder instead of a Transition.
        """
        if self.state not in states.keys():
            raise UndefinedState()
        transitions = []
        for t in self.transitions:
            # resolve brick references in composite conditions
            # For builders, SensorLevelCondition may temporarily carry names instead of objects
            if hasattr(t, 'condition') and t.condition is not None:
                cond = t.condition
                try:
                    from pyArduinoML.model.conditions import SensorLevelCondition, AnalogThresholdCondition, AndCondition, OrCondition
                    def resolve_cond(c):
                        if isinstance(c, SensorLevelCondition):
                            s = c.sensor
                            if isinstance(s, str) and s in bricks:
                                c.sensor = bricks[s]
                            return c
                        if isinstance(c, AnalogThresholdCondition):
                            s = c.sensor
                            if isinstance(s, str) and s in bricks:
                                c.sensor = bricks[s]
                            return c
                        if isinstance(c, AndCondition) or isinstance(c, OrCondition):
                            c.left = resolve_cond(c.left)
                            c.right = resolve_cond(c.right)
                            return c
                        return c
                    t.condition = resolve_cond(cond)
                except Exception:
                    pass
            transitions.append(t.get_contents(bricks, states))
        states[self.state].transitions = transitions


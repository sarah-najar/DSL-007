__author__ = 'pascalpoizat'

from pyArduinoML.model.NamedElement import NamedElement
import SIGNAL

class State(NamedElement):
    """
    A state in the application.

    """

    def __init__(self, name, actions=(), transitions=None):
        """
        Constructor.

        :param name: String, name of the state
        :param actions: List[Action], sequence of actions to do when entering the state (size should be > 0)
        :param transition: Transition, unique outgoing transition
        :return:
        """
        NamedElement.__init__(self, name)
        self.transitions = list(transitions) if transitions is not None else []
        self.actions = actions

    def settransition(self, transition):
        # kept for backward compatibility: append as single transition
        self.transitions = [transition]

    def setup(self):
        """
        Arduino code for the state.

        :return: String
        """
        rtr = ""
        rtr += "void state_%s() {\n" % self.name
        # actions
        for action in self.actions:
            if hasattr(action, 'to_arduino'):
                rtr += action.to_arduino() + "\n"
            else:
                rtr += "\tdigitalWrite(%s, %s);\n" % (action.brick.name, SIGNAL.value(action.value))
        rtr += "\tboolean guard =  millis() - time > debounce;\n"
        # transitions chain
        if len(self.transitions) > 0:
            rtr += "\tif (" + " || ".join(map(lambda t: t.condition_expr(), filter(lambda t: hasattr(t, 'condition_expr'), self.transitions))) + ") { /* composite guard present */ }\n"
        # Render each transition with priority order
        first = True
        for t in self.transitions:
            prefix = "\tif" if first else "\telse if"
            cond = t.condition_expr()
            rtr += "%s (%s) {\n\t\ttime = millis(); state_%s();\n\t}" % (prefix, cond, t.next_state_name())
            rtr += "\n"
            first = False
        # default self-loop
        rtr += "\telse {\n\t\tstate_%s();\n\t}" % self.name
        rtr += "\n}\n"
        return rtr

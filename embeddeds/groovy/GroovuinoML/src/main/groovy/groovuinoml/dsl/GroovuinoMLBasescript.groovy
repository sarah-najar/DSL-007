package groovuinoml.dsl

import io.github.mosser.arduinoml.kernel.behavioral.*
import io.github.mosser.arduinoml.kernel.structural.*

abstract class GroovuinoMLBasescript extends Script {
//	public static Number getDuration(Number number, TimeUnit unit) throws IOException {
//		return number * unit.inMillis;
//	}

    // sensor "name" pin n
    def sensor(String name) {
        [pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n) },
        onPin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n)}]
    }

    // analog sensor (potentiometer) "name" on A{index}
    def potentiometer(String name) {
        [on: { aIndex -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createAnalogSensor(name, aIndex) }]
    }

    // digital actuator "name" pin n
    def actuator(String name) {
        [pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
    }

    // buzzer "name" pin n
    def buzzer(String name) {
        [pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createBuzzer(name, n) }]
    }

    // lcd "name" size columns, rows
    def lcd(String name) {
        [size: { cols, rows -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createLCD(name, cols as int, rows as int) }]
    }
	
    // state "name" means actuator becomes signal [and ...]
    def state(String name) {
        List<Action> actions = new ArrayList<Action>()
        ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
        // recursive closure to allow multiple and statements
        def closure
        closure = { theActuator ->
            def resolveAct = { obj -> obj instanceof String ? (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable(obj) : (Actuator)obj }
            [
                becomes: { signal ->
                    Actuator act = resolveAct(theActuator)
                    ActuatorAction a = new ActuatorAction()
                    a.setActuator(act)
                    a.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
                    actions.add(a)
                    [and: closure]
                },
                shortBeep: { ->
                    def act = resolveAct(theActuator)
                    if(!(act instanceof Buzzer)) throw new IllegalArgumentException("shortBeep requires a buzzer actuator")
                    BuzzerAction a = new BuzzerAction(); a.setActuator((Buzzer)act); a.setLength(BuzzerAction.LENGTH.SHORT)
                    actions.add(a)
                    [and: closure]
                },
                longBeep: { ->
                    def act = resolveAct(theActuator)
                    if(!(act instanceof Buzzer)) throw new IllegalArgumentException("longBeep requires a buzzer actuator")
                    BuzzerAction a = new BuzzerAction(); a.setActuator((Buzzer)act); a.setLength(BuzzerAction.LENGTH.LONG)
                    actions.add(a)
                    [and: closure]
                },
                display: { text ->
                    def act = resolveAct(theActuator)
                    if(!(act instanceof LCDScreen)) throw new IllegalArgumentException("display requires an LCD screen actuator")
                    LCDAction a = new LCDAction(); a.setActuator((LCDScreen)act); a.setMessage(text as String)
                    actions.add(a)
                    [and: closure]
                }
            ]
        }
        [means: closure]
    }
	
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}
	
    // from state1 to state2 when ... / after ...
    def from(state1) {
        [to: { state2 ->
            def resolveState = { obj -> obj instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(obj) : (State)obj }
            def resolveSensor = { obj -> obj instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(obj) : (Sensor)obj }
            [
                when: { sensor ->
                    [becomes: { signal ->
                        ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
                            resolveState(state1),
                            resolveState(state2),
                            resolveSensor(sensor),
                            signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
                    }]
                },
                after: { value, unit = null ->
                    if(unit == null) {
                        ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
                            resolveState(state1), resolveState(state2), value as int)
                    } else {
                        ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
                            resolveState(state1), resolveState(state2), value as int, (TimeUnit)unit)
                    }
                },
                whenBothHigh: { s1, s2 ->
                    ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createBothHigh(
                        resolveState(state1), resolveState(state2), resolveSensor(s1), resolveSensor(s2))
                },
                whenPotAtLeast: { pot, th ->
                    def p = resolveSensor(pot)
                    ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createPotAtLeast(
                        resolveState(state1), resolveState(state2), (AnalogSensor)p, th as int)
                },
                whenPotBelow: { pot, th ->
                    def p = resolveSensor(pot)
                    ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createPotBelow(
                        resolveState(state1), resolveState(state2), (AnalogSensor)p, th as int)
                },
                whenButtonAndPotAtLeast: { button, pot, th ->
                    def b = resolveSensor(button); def p = resolveSensor(pot)
                    ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createButtonAndPotAtLeast(
                        resolveState(state1), resolveState(state2), b, (AnalogSensor)p, th as int)
                },
                whenEitherButtonLowOrPotBelow: { button, pot, th ->
                    def b = resolveSensor(button); def p = resolveSensor(pot)
                    ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createEitherButtonLowOrPotBelow(
                        resolveState(state1), resolveState(state2), b, (AnalogSensor)p, th as int)
                }
            ]
        }]
    }
	
	// export name
	def export(String name) {
		println(((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().generateCode(name).toString())
	}
	
	// disable run method while running
	int count = 0
	abstract void scriptBody()
	def run() {
		if(count == 0) {
			count++
			scriptBody()
		} else {
			println "Run method is disabled"
		}
	}
}

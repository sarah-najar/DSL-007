package main.groovy.groovuinoml.dsl;

import java.util.*;

import groovy.lang.Binding;
import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.Actuator;
import io.github.mosser.arduinoml.kernel.structural.Brick;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.Sensor;

public class GroovuinoMLModel {
	private List<Brick> bricks;
	private List<State> states;
	private State initialState;
	
	private Binding binding;
	
	public GroovuinoMLModel(Binding binding) {
		this.bricks = new ArrayList<Brick>();
		this.states = new ArrayList<State>();
		this.binding = binding;
	}
	
	public void createSensor(String name, Integer pinNumber) {
		Sensor sensor = new Sensor();
		sensor.setName(name);
		sensor.setPin(pinNumber);
		this.bricks.add(sensor);
		this.binding.setVariable(name, sensor);
//		System.out.println("> sensor " + name + " on pin " + pinNumber);
	}
	
    public void createActuator(String name, Integer pinNumber) {
        Actuator actuator = new Actuator();
        actuator.setName(name);
        actuator.setPin(pinNumber);
        this.bricks.add(actuator);
        this.binding.setVariable(name, actuator);
    }

    public void createBuzzer(String name, Integer pinNumber) {
        io.github.mosser.arduinoml.kernel.structural.Buzzer bz = new io.github.mosser.arduinoml.kernel.structural.Buzzer();
        bz.setName(name);
        bz.setPin(pinNumber);
        this.bricks.add(bz);
        this.binding.setVariable(name, bz);
    }

    public void createLCD(String name, int columns, int rows) {
        io.github.mosser.arduinoml.kernel.structural.LCDScreen lcd = new io.github.mosser.arduinoml.kernel.structural.LCDScreen();
        lcd.setName(name);
        lcd.setPin(0);
        lcd.setColumns(columns);
        lcd.setRows(rows);
        this.bricks.add(lcd);
        this.binding.setVariable(name, lcd);
    }
    
    public void createAnalogSensor(String name, Integer analogIndex) {
        io.github.mosser.arduinoml.kernel.structural.AnalogSensor sensor = new io.github.mosser.arduinoml.kernel.structural.AnalogSensor();
        sensor.setName(name);
        sensor.setPin(analogIndex);
        this.bricks.add(sensor);
        this.binding.setVariable(name, sensor);
    }

    public void createState(String name, List<Action> actions) {
		State state = new State();
		state.setName(name);
		state.setActions(actions);
		this.states.add(state);
		this.binding.setVariable(name, state);
	}
	
    public void createTransition(State from, State to, Sensor sensor, SIGNAL value) {
        SensorLevelCondition cond = new SensorLevelCondition();
        cond.setSensor(sensor);
        cond.setExpectedValue(value);
        ConditionalTransition transition = new ConditionalTransition();
        transition.setCondition(cond);
        transition.setNext(to);
        from.addTransition(transition);
    }

    public void createTransition(State from, State to, int delay) {
        TemporalTransition transition = new TemporalTransition();
        transition.setNext(to);
        transition.setDuration(delay);
        transition.setUnit(TimeUnit.ms);
        from.addTransition(transition);
    }

    public void createTransition(State from, State to, int amount, TimeUnit unit) {
        TemporalTransition transition = new TemporalTransition();
        transition.setNext(to);
        transition.setDuration(amount);
        transition.setUnit(unit == null ? TimeUnit.ms : unit);
        from.addTransition(transition);
    }

    public void createConditionalTransition(State from, State to, Condition condition) {
        ConditionalTransition t = new ConditionalTransition();
        t.setCondition(condition);
        t.setNext(to);
        from.addTransition(t);
    }
	
    // Convenience builders for composite conditions
    public void createBothHigh(State from, State to, Sensor a, Sensor b) {
        SensorLevelCondition ca = new SensorLevelCondition(); ca.setSensor(a); ca.setExpectedValue(SIGNAL.HIGH);
        SensorLevelCondition cb = new SensorLevelCondition(); cb.setSensor(b); cb.setExpectedValue(SIGNAL.HIGH);
        AndCondition and = new AndCondition(); and.setLeft(ca); and.setRight(cb);
        createConditionalTransition(from, to, and);
    }

    public void createPotAtLeast(State from, State to, io.github.mosser.arduinoml.kernel.structural.AnalogSensor pot, int threshold) {
        AnalogThresholdCondition ap = new AnalogThresholdCondition();
        ap.setSensor(pot);
        ap.setThreshold(threshold);
        ap.setAtLeast(true);
        createConditionalTransition(from, to, ap);
    }

    public void createPotBelow(State from, State to, io.github.mosser.arduinoml.kernel.structural.AnalogSensor pot, int threshold) {
        AnalogThresholdCondition ap = new AnalogThresholdCondition();
        ap.setSensor(pot);
        ap.setThreshold(threshold);
        ap.setAtLeast(false);
        createConditionalTransition(from, to, ap);
    }

    public void createButtonAndPotAtLeast(State from, State to, Sensor button, io.github.mosser.arduinoml.kernel.structural.AnalogSensor pot, int threshold) {
        SensorLevelCondition cb = new SensorLevelCondition(); cb.setSensor(button); cb.setExpectedValue(SIGNAL.HIGH);
        AnalogThresholdCondition ap = new AnalogThresholdCondition(); ap.setSensor(pot); ap.setThreshold(threshold); ap.setAtLeast(true);
        AndCondition and = new AndCondition(); and.setLeft(cb); and.setRight(ap);
        createConditionalTransition(from, to, and);
    }

    public void createEitherButtonLowOrPotBelow(State from, State to, Sensor button, io.github.mosser.arduinoml.kernel.structural.AnalogSensor pot, int threshold) {
        SensorLevelCondition cb = new SensorLevelCondition(); cb.setSensor(button); cb.setExpectedValue(SIGNAL.LOW);
        AnalogThresholdCondition ap = new AnalogThresholdCondition(); ap.setSensor(pot); ap.setThreshold(threshold); ap.setAtLeast(false);
        OrCondition or = new OrCondition(); or.setLeft(cb); or.setRight(ap);
        createConditionalTransition(from, to, or);
    }

    public void setInitialState(State state) {
        this.initialState = state;
    }
	
	@SuppressWarnings("rawtypes")
	public Object generateCode(String appName) {
		App app = new App();
		app.setName(appName);
		app.setBricks(this.bricks);
		app.setStates(this.states);
		app.setInitial(this.initialState);
		Visitor codeGenerator = new ToWiring();
		app.accept(codeGenerator);
		
		return codeGenerator.getResult();
	}
}

package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.Arrays;

public class Switch {

	public static void main(String[] args) {

		// Declaring elementary bricks
		Sensor button = new Sensor();
		button.setName("button");
		button.setPin(9);

		Actuator led = new Actuator();
		led.setName("LED");
		led.setPin(12);

		// Declaring states
		State on = new State();
		on.setName("on");

		State off = new State();
		off.setName("off");

		// Creating actions
		ActuatorAction switchTheLightOn = new ActuatorAction();
		switchTheLightOn.setActuator(led);
		switchTheLightOn.setValue(SIGNAL.HIGH);

		ActuatorAction switchTheLightOff = new ActuatorAction();
		switchTheLightOff.setActuator(led);
		switchTheLightOff.setValue(SIGNAL.LOW);

		// Binding actions to states
		on.setActions(Arrays.asList(switchTheLightOn));
		off.setActions(Arrays.asList(switchTheLightOff));

		// Creating transitions
		ConditionalTransition on2off = new ConditionalTransition();
		SensorLevelCondition cond1 = new SensorLevelCondition();
		cond1.setSensor(button); cond1.setExpectedValue(SIGNAL.HIGH);
		on2off.setNext(off);
		on2off.setCondition(cond1);

		ConditionalTransition off2on = new ConditionalTransition();
		SensorLevelCondition cond2 = new SensorLevelCondition();
		cond2.setSensor(button); cond2.setExpectedValue(SIGNAL.HIGH);
		off2on.setNext(on);
		off2on.setCondition(cond2);

        // Binding transitions to states
        on.addTransition(on2off);
        off.addTransition(off2on);

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Switch!");
		theSwitch.setBricks(Arrays.asList(button, led ));
		theSwitch.setStates(Arrays.asList(on, off));
		theSwitch.setInitial(off);

		// Generating Code
		Visitor codeGenerator = new ToWiring();
		theSwitch.accept(codeGenerator);

		// Printing the generated code on the console
		System.out.println(codeGenerator.getResult());
	}

}

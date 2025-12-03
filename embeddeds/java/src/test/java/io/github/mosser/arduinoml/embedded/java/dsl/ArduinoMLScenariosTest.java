package io.github.mosser.arduinoml.embedded.java.dsl;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;
import io.github.mosser.arduinoml.kernel.validation.ModelValidator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ArduinoMLScenariosTest {

    private String generate(App app) {
        Visitor<StringBuffer> gen = new ToWiring();
        app.accept(gen);
        return gen.getResult().toString();
    }

    @Test
    public void testSimpleAlarmScenario() {
        App app = AppBuilder
                .application("alarm")
                .uses(AppBuilder.sensor("button", 9))
                .uses(AppBuilder.actuator("led", 12))
                .uses(AppBuilder.buzzer("bz", 8))
                .hasForState("idle").setting("led").toLow().initial().endState()
                .hasForState("alarm").setting("led").toHigh().setting("bz").shortBeep().endState()
                .beginTransitionTable()
                    .from("idle").when("button").isHigh().goTo("alarm")
                    .from("alarm").after(500).goTo("idle")
                .endTransitionTable()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.isEmpty());
        String code = generate(app);
        assertTrue(code.contains("if( digitalRead(9) == HIGH"));
        assertTrue(code.contains("delay(500)"));
        assertTrue(code.contains("digitalWrite(12,HIGH)") || code.contains("digitalWrite(12, HIGH)"));
        assertTrue(code.contains("digitalWrite(8, HIGH)"));
        assertTrue(code.contains("digitalWrite(8, LOW)"));
    }

    @Test
    public void testDualButtonScenario() {
        App app = AppBuilder
                .application("dual")
                .uses(AppBuilder.sensor("b1", 2))
                .uses(AppBuilder.sensor("b2", 3))
                .uses(AppBuilder.actuator("led", 12))
                .hasForState("s1").setting("led").toLow().initial().endState()
                .hasForState("s2").setting("led").toHigh().endState()
                .beginTransitionTable()
                    .from("s1").when("b1").isHigh().goTo("s2")
                    .from("s2").when("b2").isHigh().goTo("s1")
                .endTransitionTable()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.isEmpty());
        String code = generate(app);
        assertTrue(code.contains("if( digitalRead(2) == HIGH"));
        assertTrue(code.contains("if( digitalRead(3) == HIGH"));
        assertTrue(code.contains("digitalWrite(12,HIGH)") || code.contains("digitalWrite(12, HIGH)"));
        assertTrue(code.contains("digitalWrite(12,LOW)") || code.contains("digitalWrite(12, LOW)"));
    }

    @Test
    public void testMultiStateScenario() {
        // Multi-state expected behavior:
        // Quiet --(press)--> Buzz --(press)--> Light --(press)--> Quiet -- ...
        App app = AppBuilder
                .application("multi")
                .uses(AppBuilder.sensor("button", 9))
                .uses(AppBuilder.actuator("led", 12))
                .uses(AppBuilder.buzzer("bz", 8))
                .hasForState("Quiet").setting("bz").toLow().setting("led").toLow().initial().endState()
                .hasForState("Buzz").setting("bz").toHigh().setting("led").toLow().endState()
                .hasForState("Light").setting("bz").toLow().setting("led").toHigh().endState()
                .beginTransitionTable()
                    .from("Quiet").when("button").isHigh().goTo("Buzz")
                    .from("Buzz").when("button").isHigh().goTo("Light")
                    .from("Light").when("button").isHigh().goTo("Quiet")
                .endTransitionTable()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.isEmpty());
        String code = generate(app);
        // No delays in this scenario
        assertFalse(code.contains("delay("));
        // One button check per state
        assertTrue(code.contains("if( digitalRead(9) == HIGH"));
        // Buzzer and LED commands appear
        assertTrue(code.contains("digitalWrite(8, HIGH)"));
        assertTrue(code.contains("digitalWrite(8, LOW)"));
        assertTrue(code.contains("digitalWrite(12, HIGH)"));
        assertTrue(code.contains("digitalWrite(12, LOW)"));
    }

    @Test
    public void testStateBasedScenario() {
        App app = AppBuilder
                .application("switch")
                .uses(AppBuilder.sensor("button", 9))
                .uses(AppBuilder.actuator("led", 12))
                .hasForState("off").setting("led").toLow().initial().endState()
                .hasForState("on").setting("led").toHigh().endState()
                .beginTransitionTable()
                    .from("off").when("button").isHigh().goTo("on")
                    .from("on").when("button").isHigh().goTo("off")
                .endTransitionTable()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.isEmpty());
        String code = generate(app);
        assertTrue(code.contains("switch(currentState)"));
        assertTrue(code.contains("case on"));
        assertTrue(code.contains("case off"));
        assertTrue(code.contains("if( digitalRead(9) == HIGH"));
    }

    // ------------------ Negative tests ------------------

    @Test
    public void testTransitionToUnknownStateDetectedByValidator() {
        // Build directly with kernel classes to bypass DSL checks
        Sensor button = new Sensor(); button.setName("button"); button.setPin(9);
        Actuator led = new Actuator(); led.setName("led"); led.setPin(12);

        State a = new State(); a.setName("a");
        State b = new State(); b.setName("b");

        ActuatorAction act = new ActuatorAction(); act.setActuator(led); act.setValue(SIGNAL.LOW);
        a.setActions(Arrays.asList(act));
        b.setActions(Arrays.asList(act));

        // Transition to a state not in the app
        State c = new State(); c.setName("c");
        ConditionalTransition t = new ConditionalTransition();
        SensorLevelCondition cond = new SensorLevelCondition();
        cond.setSensor(button); cond.setExpectedValue(SIGNAL.HIGH);
        t.setCondition(cond); t.setNext(c);
        a.addTransition(t);

        App app = new App();
        app.setName("invalid");
        app.setBricks(Arrays.asList(button, led));
        app.setStates(Arrays.asList(a, b));
        app.setInitial(a);

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.stream().anyMatch(e -> e.toLowerCase().contains("outside")));
    }

    @Test
    public void testLCDMessageTooLongDetected() {
        App app = AppBuilder
                .application("lcd")
                .uses(AppBuilder.lcd("screen", 8, 1))
                .hasForState("s").setting("screen").display("HELLOWORLD").initial().endState()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.stream().anyMatch(e -> e.toLowerCase().contains("lcd message too long")));
    }

    @Test
    public void testNoInitialStateDetected() {
        App app = AppBuilder
                .application("noinit")
                .uses(AppBuilder.sensor("button", 9))
                .uses(AppBuilder.actuator("led", 12))
                .hasForState("off").setting("led").toLow().endState()
                .hasForState("on").setting("led").toHigh().endState()
                .beginTransitionTable()
                    .from("off").when("button").isHigh().goTo("on")
                .endTransitionTable()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.toString(), errors.stream().anyMatch(e -> e.toLowerCase().contains("initial state")));
    }
}

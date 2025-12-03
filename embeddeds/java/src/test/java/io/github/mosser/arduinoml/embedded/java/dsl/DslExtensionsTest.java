package io.github.mosser.arduinoml.embedded.java.dsl;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.validation.ModelValidator;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DslExtensionsTest {

    private String generate(App app) {
        Visitor<StringBuffer> gen = new ToWiring();
        app.accept(gen);
        return gen.getResult().toString();
    }

    @Test
    public void testTimeAndSignalTransitionsGeneration() {
        App app = AppBuilder
                .application("test")
                .uses(AppBuilder.sensor("button", 9))
                .uses(AppBuilder.actuator("led", 12))
                .hasForState("off").setting("led").toLow().initial().endState()
                .hasForState("on").setting("led").toHigh().endState()
                .beginTransitionTable()
                    .from("off").when("button").isHigh().goTo("on")
                    .from("on").after(500).goTo("off")
                .endTransitionTable()
                .build();

        String code = generate(app);
        assertTrue(code.contains("delay(500)"));
        assertTrue(code.contains("if( digitalRead(9) == HIGH"));
    }

    @Test
    public void testBuzzerBeepGeneration() {
        App app = AppBuilder
                .application("buzz")
                .uses(AppBuilder.buzzer("bz", 8))
                .hasForState("alert").setting("bz").shortBeep().initial().endState()
                .build();

        String code = generate(app);
        assertTrue(code.contains("digitalWrite(8, HIGH)"));
        assertTrue(code.contains("delay(200)"));
        assertTrue(code.contains("digitalWrite(8, LOW)"));
    }

    @Test
    public void testValidationInitialStateAndTransitions() {
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
        assertTrue(errors.stream().anyMatch(e -> e.toLowerCase().contains("initial state")));
    }

    @Test
    public void testLcdMessageTooLongValidation() {
        App app = AppBuilder
                .application("lcd")
                .uses(AppBuilder.lcd("screen", 8, 1))
                .hasForState("s1").setting("screen").display("HELLOWORLD").initial().endState()
                .build();

        List<String> errors = ModelValidator.validate(app);
        assertTrue(errors.stream().anyMatch(e -> e.toLowerCase().contains("lcd message too long")));
    }
}

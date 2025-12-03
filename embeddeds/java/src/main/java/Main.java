import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

import java.nio.file.Files;
import java.nio.file.Paths;

import static io.github.mosser.arduinoml.embedded.java.dsl.AppBuilder.*;
import io.github.mosser.arduinoml.kernel.behavioral.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        // Choose the scenario to generate:
        App app = twoButtonsAnd();

        Visitor<StringBuffer> gen = new ToWiring();
        app.accept(gen);
        String wiring = gen.getResult().toString();

        System.out.println(wiring);
        Files.createDirectories(Paths.get("target","generated"));
        Files.write(Paths.get("target","generated", app.getName() + ".ino"), wiring.getBytes());
    }

    // ---------- Scenarios to test ----------
    private static App simpleAlarm() {
        return application("alarm")
                .uses(sensor("button", 9))
                .uses(actuator("led", 12))
                .uses(buzzer("bz", 8))
                .hasForState("idle").setting("led").toLow().initial().endState()
                .hasForState("alarm").setting("led").toHigh().setting("bz").shortBeep().endState()
                .beginTransitionTable()
                .from("idle").when("button").isHigh().goTo("alarm")
                .from("alarm").after(500).goTo("idle")
                .endTransitionTable()
                .build();
    }

    private static App dualButtons() {
        // Here: button + potentiometer (A0). Buzzer makes sound only if both are active.
        int TH = 512; // threshold for potentiometer
        return application("dual")
                .uses(sensor("b1", 12))
                .uses(potentiometer("pot", 0)) // A0
                .uses(buzzer("bz", 8))
                .hasForState("idle").setting("bz").toLow().initial().endState()
                .hasForState("alarm").setting("bz").toHigh().endState()
                .beginTransitionTable()
                .from("idle").whenButtonAndPotAtLeast("b1", "pot", TH).goTo("alarm")
                .from("alarm").whenEitherButtonLowOrPotBelow("b1", "pot", TH).goTo("idle")
                .endTransitionTable()
                .build();
    }

    private static App stateBasedSwitch() {
        return application("switch")
                .uses(sensor("button", 9))
                .uses(actuator("led", 12))
                .hasForState("off").setting("led").toLow().initial().endState()
                .hasForState("on").setting("led").toHigh().endState()
                .beginTransitionTable()
                .from("off").when("button").isHigh().goTo("on")
                .from("on").when("button").isHigh().goTo("off")
                .endTransitionTable()
                .build();
    }

    private static App multiState() {
        // Quiet --press--> Buzz --press--> Light --press--> Quiet ...
        return application("multi")
                .uses(sensor("button", 9))
                .uses(actuator("led", 12))
                .uses(buzzer("bz", 8))
                .hasForState("Quiet").setting("bz").toLow().setting("led").toLow().initial().endState()
                .hasForState("Buzz").setting("bz").toHigh().setting("led").toLow().endState()
                .hasForState("Light").setting("bz").toLow().setting("led").toHigh().endState()
                .beginTransitionTable()
                .from("Quiet").when("button").isHigh().goTo("Buzz")
                .from("Buzz").when("button").isHigh().goTo("Light")
                .from("Light").when("button").isHigh().goTo("Quiet")
                .endTransitionTable()
                .build();
    }

    // -------- Extension scenarios --------

    // Temporal blink: 500ms ON, 1s OFF alternately (demo only)
    private static App timerBlink() {
        return application("timer")
            .uses(actuator("led", 12))
            .hasForState("on").setting("led").toHigh().initial().endState()
            .hasForState("off").setting("led").toLow().endState()
            .beginTransitionTable()
                .from("on").after(500).goTo("off")
                .from("off").after(1, TimeUnit.s).goTo("on")
            .endTransitionTable()
            .build();
    }

    // Temporal transition as per acceptance scenario:
    // Press B1 -> LED turns ON, then automatically turns OFF after 800ms,
    // and waits for a new press.
    private static App temporalAfterPush() {
        return application("temporal_after_push")
            .uses(sensor("b1", 10))
            .uses(actuator("led", 12))
            .hasForState("waiting").setting("led").toLow().initial().endState()
            .hasForState("on").setting("led").toHigh().endState()
            .beginTransitionTable()
                .from("waiting").when("b1").isHigh().goTo("on")
                .from("on").after(800).goTo("waiting")
            .endTransitionTable()
            .build();
    }

    // Buzzer pattern: alternates short and long beep using temporal transitions
    private static App beepPattern() {
        return application("beeps")
            .uses(buzzer("bz", 8))
            .hasForState("short_beep").setting("bz").shortBeep().initial().endState()
            .hasForState("long_beep").setting("bz").longBeep().endState()
            .beginTransitionTable()
                .from("short_beep").after(800).goTo("long_beep")
                .from("long_beep").after(1000).goTo("short_beep")
            .endTransitionTable()
            .build();
    }

    // LCD valid demo
    private static App lcdHello() {
        return application("lcd_ok")
            .uses(lcd("screen", 16, 2))
            .hasForState("show").setting("screen").display("HELLO").initial().endState()
            .beginTransitionTable().endTransitionTable()
            .build();
    }

    // LCD invalid (message too long) — à valider via ModelValidator dans un test dédié
    private static App lcdTooLong() {
        return application("lcd_bad")
            .uses(lcd("screen", 8, 1))
            .hasForState("show").setting("screen").display("HELLOWORLD").initial().endState()
            .beginTransitionTable().endTransitionTable()
            .build();
    }

    // Two digital buttons in AND to light a LED
    private static App twoButtonsAnd() {
        return application("two_and")
            .uses(sensor("b1", 2))
            .uses(sensor("b2", 3))
            .uses(actuator("led", 12))
            .hasForState("idle").setting("led").toLow().initial().endState()
            .hasForState("on").setting("led").toHigh().endState()
            .beginTransitionTable()
                .from("idle").whenBothHigh("b1", "b2").goTo("on")
                .from("on").when("b1").isLow().goTo("idle")
                .from("on").when("b2").isLow().goTo("idle")
            .endTransitionTable()
            .build();
    }

    // Potentiometer-only threshold control of LED
    private static App potThreshold() {
        int TH = 600;
        return application("pot_only")
            .uses(potentiometer("pot", 0))
            .uses(actuator("led", 12))
            .hasForState("low").setting("led").toLow().initial().endState()
            .hasForState("high").setting("led").toHigh().endState()
            .beginTransitionTable()
                .from("low").whenPotAtLeast("pot", TH).goTo("high")
                .from("high").whenPotBelow("pot", TH).goTo("low")
            .endTransitionTable()
            .build();
    }
}

package io.github.mosser.arduinoml.embedded.java.dsl;


import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.State;
import io.github.mosser.arduinoml.kernel.structural.Actuator;
import io.github.mosser.arduinoml.kernel.structural.Brick;
import io.github.mosser.arduinoml.kernel.structural.Sensor;
import io.github.mosser.arduinoml.kernel.structural.Buzzer;
import io.github.mosser.arduinoml.kernel.structural.LCDScreen;
import io.github.mosser.arduinoml.kernel.structural.AnalogSensor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppBuilder {

    App theApp = null;

    /*********************
     ** Creating an App **
     *********************/

    public static AppBuilder application(String name) {
        AppBuilder inst = new AppBuilder();
        inst.theApp = new App();
        inst.theApp.setName(name);
        return inst;
    }

    public App build() { return theApp; }

    private AppBuilder() { }

    /**********************
     ** Declaring Bricks **
     **********************/

    public AppBuilder uses(Brick b) {
        this.theApp.getBricks().add(b);
        return this;
    }

    public static Brick sensor(String name, int port) { return createBrick(Sensor.class, name, port);  }
    public static Brick actuator(String name, int port) { return createBrick(Actuator.class, name, port);  }
    public static Brick buzzer(String name, int port) { return createBrick(Buzzer.class, name, port); }
    public static Brick potentiometer(String name, int analogPin) { return createAnalogBrick(AnalogSensor.class, name, analogPin); }
    public static Brick lcd(String name, int columns, int rows) {
        try {
            LCDScreen lcd = LCDScreen.class.newInstance();
            if(name.isEmpty() || !Character.isLowerCase(name.charAt(0)))
                throw new IllegalArgumentException("Illegal brick name: ["+name+"]");
            lcd.setName(name);
            lcd.setPin(0); // no direct pin mapping here
            if (columns <= 0 || rows <= 0)
                throw new IllegalArgumentException("Illegal LCD dimension: ["+columns+"x"+rows+"]");
            lcd.setColumns(columns);
            lcd.setRows(rows);
            return lcd;
        } catch (InstantiationException | IllegalAccessException iae) {
            throw new IllegalArgumentException("Unable to instantiate LCDScreen");
        }
    }

    private static Brick createBrick(Class< ? extends Brick> kind, String name, int port) {
        try {
            Brick b = kind.newInstance();
            if(name.isEmpty() || !Character.isLowerCase(name.charAt(0)))
                throw new IllegalArgumentException("Illegal brick name: ["+name+"]");
            b.setName(name);
            if(port < 1 || port > 12)
                throw new IllegalArgumentException("Illegal brick port: ["+port+"]");
            b.setPin(port);
            return b;
        } catch (InstantiationException | IllegalAccessException iae) {
            throw new IllegalArgumentException("Unable to instantiate " + kind.getCanonicalName());
        }
    }

    private static Brick createAnalogBrick(Class<? extends Brick> kind, String name, int analogPin) {
        try {
            Brick b = kind.newInstance();
            if(name.isEmpty() || !Character.isLowerCase(name.charAt(0)))
                throw new IllegalArgumentException("Illegal brick name: ["+name+"]");
            b.setName(name);
            if(analogPin < 0 || analogPin > 5)
                throw new IllegalArgumentException("Illegal analog pin: A"+analogPin);
            b.setPin(analogPin); // store raw analog index (0..5)
            return b;
        } catch (InstantiationException | IllegalAccessException iae) {
            throw new IllegalArgumentException("Unable to instantiate " + kind.getCanonicalName());
        }
    }


    /**********************
     ** Declaring States **
     **********************/

    public StateBuilder hasForState(String name) { return new StateBuilder(this, name); }

    /*******************************
     ** Declaring TransitionTable **
     *******************************/

    public TransitionTableBuilder beginTransitionTable() {

        Map<String, State> stateTable =
                theApp.getStates().stream().collect(Collectors.toMap(State::getName, Function.identity()));

        Map<String, Sensor> sensorTable =
                theApp.getBricks().stream()
                        .filter(b -> b instanceof Sensor)
                        .map( b -> (Sensor) b)
                        .collect(Collectors.toMap(Brick::getName, Function.identity()));

        return new TransitionTableBuilder(this, stateTable, sensorTable);
    }


    /***********************************************************************************
     ** Helpers to avoid a symbol table for Bricks (using the App under construction) **
     ***********************************************************************************/

    Optional<Actuator> findActuator(String name) {
        Optional<Brick> b = theApp.getBricks()
                .stream()
                    .filter( brick    -> brick instanceof Actuator)
                    .filter( actuator -> actuator.getName().equals(name))
                .findFirst();
        return b.map(sensor -> Optional.of((Actuator) sensor)).orElse(Optional.empty());
    }

}

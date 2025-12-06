from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("pot_only") \
        .potentiometer("pot").on(0) \
        .actuator("led").on_pin(12) \
        .state("low").set("led").to(LOW) \
            .whenPotAtLeast("pot", 600).go_to_state("high") \
        .state("high").set("led").to(HIGH) \
            .whenPotBelow("pot", 600).go_to_state("low") \
        .initial("low") \
        .get_contents()

if __name__ == '__main__':
    print(app())


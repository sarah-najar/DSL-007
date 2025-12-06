from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("timer") \
        .actuator("led").on_pin(12) \
        .state("on").set("led").to(HIGH) \
            .after(500).go_to_state("off") \
        .state("off").set("led").to(LOW) \
            .after(1000).go_to_state("on") \
        .initial("on") \
        .get_contents()

if __name__ == '__main__':
    print(app())


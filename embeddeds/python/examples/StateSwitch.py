from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("switch") \
        .sensor("button").on_pin(9) \
        .actuator("led").on_pin(12) \
        .state("off").set("led").to(LOW) \
            .when("button").becomes(HIGH).go_to_state("on") \
        .state("on").set("led").to(HIGH) \
            .when("button").becomes(HIGH).go_to_state("off") \
        .initial("off") \
        .get_contents()

if __name__ == '__main__':
    print(app())


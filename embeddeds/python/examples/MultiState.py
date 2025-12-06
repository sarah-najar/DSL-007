from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("multi") \
        .sensor("button").on_pin(9) \
        .actuator("led").on_pin(12) \
        .buzzer("bz").on_pin(8) \
        .state("Quiet").set("bz").to(LOW).set("led").to(LOW) \
            .when("button").becomes(HIGH).go_to_state("Buzz") \
        .state("Buzz").set("bz").to(HIGH).set("led").to(LOW) \
            .when("button").becomes(HIGH).go_to_state("Light") \
        .state("Light").set("bz").to(LOW).set("led").to(HIGH) \
            .when("button").becomes(HIGH).go_to_state("Quiet") \
        .initial("Quiet") \
        .get_contents()

if __name__ == '__main__':
    print(app())


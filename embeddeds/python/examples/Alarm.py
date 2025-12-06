from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("alarm") \
        .sensor("button").on_pin(9) \
        .actuator("led").on_pin(12) \
        .buzzer("bz").on_pin(8) \
        .state("idle") \
            .set("led").to(LOW) \
        .state("alarm") \
            .set("led").to(HIGH) \
            .set("bz").shortBeep() \
        .state("idle") \
            .when("button").becomes(HIGH).go_to_state("alarm") \
        .state("alarm") \
            .when("button").becomes(LOW).go_to_state("idle") \
        .initial("idle") \
        .get_contents()

if __name__ == '__main__':
    print(app())


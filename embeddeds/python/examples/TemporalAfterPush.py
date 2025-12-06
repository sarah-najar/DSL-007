from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("temporal_after_push") \
        .sensor("b1").on_pin(10) \
        .actuator("led").on_pin(12) \
        .state("waiting").set("led").to(LOW) \
            .when("b1").becomes(HIGH).go_to_state("on") \
        .state("on").set("led").to(HIGH) \
            .after(800).go_to_state("waiting") \
        .initial("waiting") \
        .get_contents()

if __name__ == '__main__':
    print(app())


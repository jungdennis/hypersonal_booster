#! /usr/bin/python2

import sys
import time

EMULATE_HX711=False

referenceUnit = 1

if not EMULATE_HX711:
    import RPi.GPIO as GPIO
    from hx711 import HX711
else:
    from emulated_hx711 import HX711

def cleanAndExit():
    print("Cleaning...")

    if not EMULATE_HX711:
        GPIO.cleanup()
        
    print("Bye!")
    sys.exit()

hx = HX711(26, 19)

hx.set_reading_format("MSB", "MSB")

hx.set_reference_unit(399)
#hx.set_reference_unit(referenceUnit)

hx.reset()
hx.tare()

print("Tare done! Add weight now...")
count = 0
while True:
    try:
        val = hx.get_weight(5)
        print(val)
        hx.power_down()
        hx.power_up()
        time.sleep(0.1)
        count += 1

    except (count==100):
        cleanAndExit()

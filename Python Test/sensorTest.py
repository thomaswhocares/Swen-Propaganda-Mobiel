# -*- coding: utf-8 -*-
#/usr/bin/python3

# ROT       +
# SCHWARZ   -
# GELB      Data hig/low

import RPi.GPIO as GPIO
import time

try:
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(16, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

    while True:
        # wird eine Reflektion erkannt wird 0 ausgegeben. Wird nichts erkannt wird 1 ausgegeben.
        print(GPIO.input(16))

except KeyboardInterrupt:
        GPIO.cleanup()

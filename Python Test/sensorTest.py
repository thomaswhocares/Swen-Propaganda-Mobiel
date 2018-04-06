# -*- coding: utf-8 -*-
#/usr/bin/python3
import RPi.GPIO as GPIO
import time

try:
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(16, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

    while True:
        print(GPIO.input(16))

except KeyboardInterrupt:
        GPIO.cleanup()

#1/usr/bin/python3
import RPi.GPIO as gpio
import time

#Alles was mit Pins zu tun hat sollte mit try umgeben werden
try:
    gpio.setmode(gpio.BOARD)

    #
    gpio.setup(29, gpio.OUT)
    gpio.setup(33, gpio.OUT)
    gpio.setup(37, gpio.OUT)

    #
    for i in range(2):
        gpio.output(29, gpio.HIGH)
        print("Pin 29 an")
        time.sleep(1)
        gpio.output(29, gpio.LOW)
        print("Pin 29 aus")

        print("")
        
        
        gpio.output(33, gpio.HIGH)
        print("Pin 33 an")
        time.sleep(1)
        gpio.output(33, gpio.LOW)
        print("Pin 33 aus")

        print("")
        

        gpio.output(37, gpio.HIGH)
        print("Pin 37 an")
        time.sleep(1)
        gpio.output(33, gpio.HIGH)
        print("Pin 33 an")
        time.sleep(1)
        gpio.output(33, gpio.LOW)
        print("Pin 33 aus")
        gpio.output(37, gpio.LOW)
        print("Pin 37 aus")
        
        print("")

except KeyboardInterrupt:
    #GIPO pins werden wieder frei gegeben.
    gpio.cleanup()

# -*- coding: utf-8 -*-
#/usr/bin/python3
import RPi.GPIO as GPIO
import time
# Orange PWM
# Rot    +
# Braun  -
# pin 21 PWM Rechts
# pin 19 PWM Links

# t = time for sleep
def move_forwards(t):

        wheel_right.ChangeDutyCycle(4)
        print("forwards")

        wheelLeft.ChangeDutyCycle(8)
        time.sleep(t)

def move_backwards(t):
        
        wheel_right.ChangeDutyCycle(8)
        print("backwards")

        wheelLeft.ChangeDutyCycle(4)
        time.sleep(t)

def move_forward_left(t):

        wheel_right.ChangeDutyCycle(4)
        print("left")

        wheelLeft.ChangeDutyCycle(6)
        time.sleep(t)

def move_forward_right(t):

        wheel_right.ChangeDutyCycle(6)
        print("right")

        wheelLeft.ChangeDutyCycle(8)
        time.sleep(t)

def move_backwards_left(t):
        
        wheel_right.ChangeDutyCycle(8)
        print("zurück")

        wheelLeft.ChangeDutyCycle(6)
        time.sleep(t)

def move_backwards_right(t):
        
        wheel_right.ChangeDutyCycle(6)
        print("zurück")

        wheelLeft.ChangeDutyCycle(4)
        time.sleep(t)



try:
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(21,GPIO.OUT)#rechts
        GPIO.setup(19,GPIO.OUT)#links

        wheel_right=GPIO.PWM(21 ,50) #rechts
        wheel_right.start(100)

        wheelLeft=GPIO.PWM(19 ,50) #links
        wheelLeft.start(100)
        
        while True:
                move_forward_left(0.5)
                
except KeyboardInterrupt:
        GPIO.cleanup()


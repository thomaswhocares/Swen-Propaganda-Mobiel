# -*- coding: utf-8 -*-
#/usr/bin/python3
import RPi.GPIO as GPIO
import time


# t = time for sleep
def move_forwards(t):

        p1.ChangeDutyCycle(4)
        print("forwards")

        p2.ChangeDutyCycle(8)
        time.sleep(t)

def move_backwards(t):
        
        p1.ChangeDutyCycle(8)
        print("backwards")

        p2.ChangeDutyCycle(4)
        time.sleep(t)

def move_forward_left(t):

        p1.ChangeDutyCycle(4)
        print("left")

        p2.ChangeDutyCycle(6)
        time.sleep(t)

def move_forward_right(t):

        p1.ChangeDutyCycle(6)
        print("right")

        p2.ChangeDutyCycle(8)
        time.sleep(t)

def move_backwards_left(t):
        
        p1.ChangeDutyCycle(8)
        print("zurück")

        p2.ChangeDutyCycle(6)
        time.sleep(t)

def move_backwards_right(t):
        
        p1.ChangeDutyCycle(6)
        print("zurück")

        p2.ChangeDutyCycle(4)
        time.sleep(t)



try:
        while True:
                
                GPIO.setmode(GPIO.BCM)
                GPIO.setup(21,GPIO.OUT)
                GPIO.setup(19,GPIO.OUT)

                p1=GPIO.PWM(21 ,50) #rechts
                p1.start(100)

                p2=GPIO.PWM(19 ,50) #links
                p2.start(100)
                
                move_forward_left(0.5)
                
                
except KeyboardInterrupt:
        GPIO.cleanup()


import RPi.GPIO as GPIO
import time

class Interpreter:

    def __init__(self):
        global wheel_left,wheel_right
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(21,GPIO.OUT)#rechts
        GPIO.setup(19,GPIO.OUT)#links
        
        wheel_right=GPIO.PWM(21 ,50) #rechts
        wheel_right.start(100)

        wheel_left=GPIO.PWM(19 ,50) #links
        wheel_left.start(100)
        print("tets")
        
    def __end__(self):
        print("cleaning")
        GPIO.cleanup()

    def execute_wheel_command(self,byte_command):
        if byte_command == int('000', 2):
            wheel_left.ChangeDutyCycle(6)
            print("Left Stop")

        elif byte_command == int('100', 2):
            wheel_right.ChangeDutyCycle(6)
            print("Right Stop")

        elif byte_command == int('001', 2):
            wheel_left.ChangeDutyCycle(8)
            print("Left Forwards")

        elif byte_command == int('101', 2):
            wheel_right.ChangeDutyCycle(4)
            print("Right Forwards")

        elif byte_command == int('010', 2):
            wheel_left.ChangeDutyCycle(4)
            print("Left Backwards")

        elif byte_command == int('110', 2):
            wheel_right.ChangeDutyCycle(8)
            print("Right Backwards")

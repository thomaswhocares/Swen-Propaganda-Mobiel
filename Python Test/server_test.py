import socket
from threading import Thread
import RPi.GPIO as GPIO
import time
# Beispiel Pyhon Socket
# Wartet auf eine verbindung und gibt aus was empfangen wurde.

MAX_LENGTH = 1
PORT = 10000
t=0.1
# gibt die locale ip zurück copy pasta von stack overflow
HOST = [(s.connect(('8.8.8.8', 53)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]

left_forwards = False
left_backwards = False
right_forwards = False
right_backwards = False


def left_wheel_thread():
    while True:
        if left_forwards == True:
            wheel_left.ChangeDutyCycle(8)
    
        elif left_backwards == True:
            wheel_left.ChangeDutyCycle(4)

        else:
            wheel_left.ChangeDutyCycle(6)
            
def right_wheel_thread():
    while True:
        if right_forwards == True:
            wheel_right.ChangeDutyCycle(4)
        elif right_backwards == True:
            wheel_right.ChangeDutyCycle(8)
        else:
            wheel_right.ChangeDutyCycle(6)
        

def handle(self,clientsocket):
    print("Client verbunden.")
    while 1:
        buf = clientsocket.recv(MAX_LENGTH)
        if len(buf)<1:
            print("Client getrennt.")
            serversocket.detach
            return #client terminated connection

        if buf[0]==int('000', 2):
            global left_forwards,left_backwards
            left_forwards = False
            left_backwards = False
            print("Left Stop")

        elif buf[0]==int('100', 2):
            global right_forwards,right_backwards
            right_forwards = False
            right_backwards = False
            print("Right Stop")

        elif buf[0]==int('001', 2):
            global left_forwards
            left_forwards = True
            print("Left Forwards")

        elif buf[0]==int('101', 2):
            global right_forwards
            right_forwards = True
            print("Right Forwards")

        elif buf[0]==int('010', 2):
            global left_backwards
            left_backwards = True
            print("Left Backwards")

        elif buf[0]==int('110', 2):
            global right_backwards
            right_backwards = True
            print("Right Backwards")

# kp
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(10)
x = True

try:
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(21,GPIO.OUT)#rechts
    GPIO.setup(19,GPIO.OUT)#links

    wheel_right=GPIO.PWM(21 ,50) #rechts
    wheel_right.start(100)

    wheel_left=GPIO.PWM(19 ,50) #links
    wheel_left.start(100) 
except KeyboardInterrupt:
    GPIO.cleanup()

while x:
    try:
        print("Server gestartet.")
        #accept connections from outside
        (clientsocket, address) = serversocket.accept()
        #                   funktion für Thread  übergabewerte für die Funktion
        socketThread = Thread(target=handle, args=(clientsocket,))
        socketThread.run()

    except KeyboardInterrupt:
        print("\nShutdown wegen KeyboardInterrupt")
        
        x = False

    finally:
        serversocket.detach
    

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
HOST = "192.168.1.1"

def handle(clientsocket):
    print("Client verbunden.")
    while 1:
        buf = clientsocket.recv(MAX_LENGTH)
        if len(buf)<1:
            print("Client getrennt.")
            serversocket.detach
            return #client terminated connection

        if buf[0]==int('000', 2):
            wheel_left.ChangeDutyCycle(6)
            print("Left Stop")

        elif buf[0]==int('100', 2):
            wheel_right.ChangeDutyCycle(6)
            print("Right Stop")

        elif buf[0]==int('001', 2):
            wheel_left.ChangeDutyCycle(8)
            print("Left Forwards")

        elif buf[0]==int('101', 2):
            wheel_right.ChangeDutyCycle(4)
            print("Right Forwards")

        elif buf[0]==int('010', 2):
            wheel_left.ChangeDutyCycle(4)
            print("Left Backwards")

        elif buf[0]==int('110', 2):
            wheel_right.ChangeDutyCycle(8)
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
    global wheel_right,wheel_left
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
        print("incomming Connection")
        #                   funktion für Thread  übergabewerte für die Funktion
        socketThread = Thread(target=handle, args=(clientsocket,))
        socketThread.run()

    except KeyboardInterrupt:
        print("\nShutdown wegen KeyboardInterrupt")
        GPIO.cleanup()
        x = False
	
    finally:
        serversocket.detach
        GPIO.cleanup()
    

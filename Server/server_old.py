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

    

# kp
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(10)
x = True
while x:
    try:
        print("Server gestartet.")
        #accept connections from outside
        
        (clientsocket, address) = serversocket.accept()
        print("incomming Connection from " + address )
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
    

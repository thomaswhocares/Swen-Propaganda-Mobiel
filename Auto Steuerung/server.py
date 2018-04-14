# -*- coding: utf-8 -*-
#/usr/bin/python3

import socket
from threading import Thread


MAX_LENGTH = 1
PORT = 10000
# gibt die locale ip zurück copy pasta von stack overflow
HOST = [(s.connect(('8.8.8.8', 53)), s.getsockname()[0], s.close()) for s in [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)]][0][1]

def handle(clientsocket):
    print("Client verbunden.")
    while 1:
        buf = clientsocket.recv(MAX_LENGTH)
        if len(buf)<1:
            print("Client getrennt.")
            serversocket.detach
            return #client terminated connection
        print (buf[0])

        if buf[0]==int('000', 2):
            print("Left Stop")

        elif buf[0]==int('001', 2):
            print("LeftForwards")
        
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
        #                   funktion für Thread  übergabewerte für die Funktion
        socketThread = Thread(target=handle, args=(clientsocket,))
        socketThread.run()

    except KeyboardInterrupt:
        print("\nShutdown wegen KeyboardInterrupt")
        
        x = False

    finally:
        serversocket.detach
    
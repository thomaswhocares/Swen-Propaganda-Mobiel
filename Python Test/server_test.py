import socket
from threading import Thread
# Beispiel Pyhon Socket
# Wartet auf eine verbindung und gibt aus was empfangen wurde.

MAX_LENGTH = 4096

def handle(clientsocket):
    print("Client verbunden.")
    while 1:
        buf = clientsocket.recv(MAX_LENGTH)
        if len(buf)<1:
            print("Client getrennt.")
            serversocket.detach
            return #client terminated connection
        print (buf)

# kp
serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

PORT = 10000
HOST = '127.0.0.1'

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
    

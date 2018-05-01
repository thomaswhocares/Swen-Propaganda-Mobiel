import socket
import time
from threading import Thread
class Server:
    def __init__(self,interpreter):
        self.interpreter = interpreter
        global max_length, server_host,server_port,lissener_thread
        max_length = 1
        server_host = "192.168.1.1"
        server_port = 10000

        lissener_thread = self.Connection_lisener_thread(self.interpreter)
        lissener_thread.start()

    def __end__(self):
        lissener_thread.__end__()
        
    class Connection_lisener_thread(Thread):
        def __init__(self,interpreter):
            self.keep_listening = True
            self.connected_instances=0
            self.interpreter = interpreter

            try:
                self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                self.server_socket.bind((server_host, server_port))
                self.server_socket.listen(10)
                print("Socket erstellt")
                Thread.__init__(self)
                
            except:
                print("Socket nicht frei")
                self.interpreter.__end__
        
        def __end__(self):
            print("Beende Sockets")
            self.keep_listening = False
            self.server_socket.shutdown(2)
            self.server_socket.close()
                    
        def run(self):
            self.commandThreads = []
            while self.keep_listening:
                print("warte auf verbindung")
                try:
                    (self.clientsocket, self.address) = self.server_socket.accept()
                except:
                    print("Connection lissener down.")
                    break
                class Connection_recive_command_thread(Thread):
                    def __init__(self,interpreter,clientsocket,address):
                        self.client_connected = True
                        self.interpreter = interpreter
                        self.clientsocket = clientsocket
                        self.address = address
                        Thread.__init__(self)
                    
                    def __end__(self):
                        self.client_connected = False
                        print("Löse Socket")
                        self.clientsocket.detach()

                    def run(self):
                        while self.client_connected:
                            buf = self.clientsocket.recv(max_length)
                            if len(buf)<1:
                                print("Client getrennt.")
                                self.__end__() #client terminated connection
                            else:
                                self.interpreter.execute_wheel_command(buf[0])
                            
                
                self.commandThreads.append(Connection_recive_command_thread(self.interpreter,self.clientsocket,self.address))
                self.commandThreads[len(self.commandThreads)-1].start()
                print("client nummer "+str(self.connected_instances)+" verbunden. Platz nummer "+str(len(self.commandThreads)))
                self.connected_instances=self.connected_instances+1
                


                
          
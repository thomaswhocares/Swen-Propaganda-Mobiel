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
            global keep_listening,server_socket
            keep_listening = True
            self.connected_instances=0
            self.interpreter = interpreter

            print("Warte auf erste verbindung")
            server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            server_socket.bind((server_host, server_port))
            server_socket.listen(10)
            
            Thread.__init__(self)
        
        def __end__(self):
            global keep_listening
            keep_listening = False
            server_socket.close()
                    
        def run(self):
            while keep_listening:
                print("warte auf verbindung")
                (clientsocket, address) = server_socket.accept()
                class Connection_recive_command_thread(Thread):
                    def __init__(self,interpreter):
                        self.client_connected = True
                        self.interpreter = interpreter
                        Thread.__init__(self)
                    
                    def __end__(self):
                        self.client_connected = False
                        print("Löse Socket")
                        clientsocket.detach()

                    def run(self):
                        while self.client_connected:
                            buf = clientsocket.recv(max_length)
                            if len(buf)<1:
                                print("Client getrennt.")
                                self.__end__() #client terminated connection
                            print(buf[0])
                            self.interpreter.execute_wheel_command(buf[0])
                            
                commandThreads = []
                commandThreads.append(Connection_recive_command_thread(self.interpreter))
                commandThreads[len(commandThreads)-1].start()
                print("client nummer "+str(self.connected_instances)+" verbunden. platz nummer "+str(len(commandThreads)))
                self.connected_instances=self.connected_instances+1
                


                
          
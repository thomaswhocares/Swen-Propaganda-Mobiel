import socket
class Server:

    message = "Null"
    
    def __init__(self,msg):
        global message
        message = msg

    def print_msg(self):
        print(message)
        print(self.message)

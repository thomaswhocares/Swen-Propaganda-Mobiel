import socket
import sys


HOST = '192.168.2.171'
PORT = 10000
s = socket.socket()
s.connect((HOST, PORT))

#while 1:
#    msg = input("Command To Send: ")
#    if msg == "close":
#       s.close()
#       sys.exit(0)
#    s.send(msg.encode())

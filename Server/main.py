import interpreter
import server
import time

turner = interpreter.Interpreter()
srv = server.Server(turner)

try:
    while True:
        time.sleep(4)

except KeyboardInterrupt:
    print("Stopping everything")
    turner.__end__()
    srv.__end__()
    




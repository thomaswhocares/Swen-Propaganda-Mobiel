import interpreter

turner = interpreter.Interpreter()

try:
    
    turner.execute_wheel_command(int('101', 2))
    while True:
        print("kek")

except KeyboardInterrupt:
    turner.__end__()




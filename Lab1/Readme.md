Lab1: Tasks synchronization and parallelization

The aim of this lab is to correctly organize computation using multiply processes/threads.

Variant 12 : Use Java, processes, sockets(Java.net.Socket) and blocking IO. The main process is a socket server using java.util.concurrent.Executor

Two ways of application execution:
        
    1) Build project using Maven(pom.xml file provided), then run compiled file of Demo class(will be located in target/classes/os/lab1/cancellation/dialog
        cmd expample(running from Lab1 directory): java -cp target/classes os.lab1.cancellation.dialog.Demo
    2) run Lab1.jar file of Demo class(located in out/artifacts/Lab1_Demo)
        cmd expample(running from Lab1 directory): java -jar ./out/artifacts/Lab1_Demo/Lab1.jar
        
Cancellation: 
     
    Cancellation done using dialog initiation by special key(Ctrl+C). Interval for response is 10s(you can override it in Constants class) 
    Note: cancellation will be working properly only, if you executing app from terminal(Hotkeys do not work in IDE)

Built-in functions:

    IntOps class placed in os.lab1.compfunc.advanced, contains two functions: trialF, trialG
    Operation: integer numbers multiplication

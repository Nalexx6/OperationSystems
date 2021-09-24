package org.nalexx6;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class FunctionClient {

    private Function <Integer, Integer> function;
    private final AtomicBoolean computationStarted;
    private final AtomicBoolean computationFinished;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Integer value;

    FunctionClient(String ip, int port) throws java.io.IOException {
        computationStarted = new AtomicBoolean(false);
        computationFinished = new AtomicBoolean(false);

        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        value = new Integer (in.readLine());
        assignFunction();
    }

    private void assignFunction(){
        function = x -> x * x;
    }

    private Integer getResult(){
        computationStarted.set(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer result = function.apply(value);
        computationFinished.set(true);

        return result;
    }


}

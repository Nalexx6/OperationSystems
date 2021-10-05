package org.nalexx6.calculation;


import org.nalexx6.Constants;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

public class FunctionClient {

    private Function <Integer, Integer> function;
    private Integer value;
    private Integer result;

    public FunctionClient(String type) {
        assignFunction(type);
    }

    private void assignFunction(String type){
        if(type.equals("f")) {
            function = x -> x * x;
        }

        if (type.equals("g")) {
            function = x -> x * (x + 1);
        }
    }

    void computeResult() {

        System.out.println(Thread.currentThread().getName() + ": Connecting to server");
        try (
            Socket socket = new Socket(Constants.IP, Constants.PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            value = Integer.parseInt(in.readLine());
            System.out.println(Thread.currentThread().getName() + ": Value is read: " + value);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        this.result = function.apply(value);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        passResultToChannels();
    }

    private void passResultToChannels(){
        System.out.println(Thread.currentThread().getName() + ": Connecting to server");
        try (
                Socket socket = new Socket(Constants.IP, Constants.PORT);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())))
        {
            out.write(result.toString());
            System.out.println(Thread.currentThread().getName() + ": Value is written to the socket: " + value);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

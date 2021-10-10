package org.nalexx6.calculation;


import org.nalexx6.Constants;

import java.io.*;
import java.net.Socket;

import java.security.SecureRandom;
import java.util.function.Function;

public class FunctionClient {

    private Function <Integer, String> function;
    SecureRandom random;
    private Integer value;
    private String result;

    public FunctionClient(String type) {
        random = new SecureRandom();
        assignFunction(type);
    }

    private void assignFunction(String type){
        if(type.equals("f")) {
            function = x -> (x == 1 ? fExample(x) : Integer.toString(x * x));
        }

        if (type.equals("g")) {
            function = x ->  Integer.toString(x * (x + 1));
        }
    }

    private String fExample(Integer x){
        if(random.nextInt(5) < 3) {
            return "soft fail";
        } else {
            return Integer.toString(x + 8);
        }
    }

    void computeResult() {

        System.out.println(Thread.currentThread().getName() + ": Connecting to server");
        try (
            Socket socket = new Socket(Constants.IP, Constants.PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
        {
            value = (Integer) in.readObject();
            System.out.println(Thread.currentThread().getName() + ": Value is read: " + value);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
//        value = 2;

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
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()))
        {
            out.writeObject(result);
            System.out.println(Thread.currentThread().getName() + ": Value is written to the socket: " + value);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(result);
    }

}

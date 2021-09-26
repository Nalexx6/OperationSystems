package org.nalexx6;

import org.nalexx6.calculation.FunctionClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class ComputationManager {

    private final ExecutorService executorService;
    private final ServerSocket server;

    private BiFunction<Integer, Integer, Integer> bitwiseOperation;
    private final Integer value;
    private final List<Future<Integer>> functionFutures;
    private final List<Integer> functionResults;

    ComputationManager(Integer inputValue) throws IOException {
        this.value = inputValue;
        functionFutures = new ArrayList<>();
        functionResults = new ArrayList<>();
        assignBitwiseOperation();

        System.out.println("Starting socket server");
        server = new ServerSocket(Constants.PORT);
        executorService = Executors.newFixedThreadPool(2);

    }

    private void assignBitwiseOperation(){
        bitwiseOperation = (x, y) -> x * y;
    }

    public void startComputing(){
        List<Callable<Integer>> tasks = assignTasks();

        for(Callable<Integer> task: tasks){
            Future<Integer> future = executorService.submit(task);
            functionFutures.add(future);
        }

        passValueToChannels();

        while (!computationIsDone()){
            //pass
        }

        for(Future<Integer> f: functionFutures){
            try {
                functionResults.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

    }

    private List<Callable<Integer>> assignTasks(){
        Callable<Integer> fFunction = () -> {

            FunctionClient function = new FunctionClient("f");
            return function.getResult();
        };

        Callable<Integer> gFunction = () -> {

            FunctionClient function = new FunctionClient("g");
            return function.getResult();
        };

        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(fFunction);
        tasks.add(gFunction);
        return tasks;
    }

    private void passValueToChannels(){
        System.out.println("Passing value to sockets");

        for(int i = 0; i < 2; i++) {
            try (
                Socket socket = server.accept();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
            ){
                out.write(value.toString());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Values passed");
    }

    private boolean computationIsDone(){
        boolean allDone = true;

        for(Future<Integer> f: functionFutures){
                allDone &= f.isDone();
        }
        return allDone;
    }

    public Integer getResult(){
        return bitwiseOperation.apply(functionResults.get(0), functionResults.get(1));
    }
}

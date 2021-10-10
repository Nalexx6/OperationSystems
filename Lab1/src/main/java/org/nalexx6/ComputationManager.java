package org.nalexx6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class ComputationManager {

    private final ExecutorService executorService;
    private final ServerSocket server;

    private BiFunction<Integer, Integer, Integer> bitwiseOperation;
    private final Integer value;
    private final List<Future<?>> functionFutures;
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
        List<Runnable> tasks = assignTasks();

        for(Runnable task: tasks){
            Future<?> future = executorService.submit(task);
            functionFutures.add(future);
        }

        passValueToChannels();

        while (true){
            if(computationIsDone()){
                readResultsFromChannels();
                break;
            }
        }

        executorService.shutdown();
    }

    private List<Runnable> assignTasks(){
        String classPath =
                Objects.requireNonNull(ComputationManager.class.getClassLoader().getResource(".")).toString();

        Runnable fProcess = () -> {
            System.out.println("Starting F process");
            ProcessBuilder processBuilderF = new ProcessBuilder("java", "-cp",
                    classPath, "org.nalexx6.calculation.FProcess");
            try {
                processBuilderF.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable gProcess = () -> {
            System.out.println("Starting G process");
            ProcessBuilder processBuilderG = new ProcessBuilder("java", "-cp",
                    classPath, "org.nalexx6.calculation.GProcess");
            try {
                processBuilderG.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        List<Runnable> tasks = new ArrayList<>();
        tasks.add(fProcess);
        tasks.add(gProcess);
        return tasks;
    }

    private void passValueToChannels(){
        System.out.println("Passing value to sockets");

        for(int i = 0; i < 2; i++) {
            try (
                Socket socket = server.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ){
                out.write(value);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Values passed");
    }

    private boolean computationIsDone(){
        boolean allDone = true;

        for(Future<?> f: functionFutures){
                allDone &= f.isDone();
        }
        return allDone;
    }

    private void readResultsFromChannels(){
        for(int i = 0; i < 2; i++) {
            try (Socket socket = server.accept();
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                Integer res = in.readInt();
                functionResults.add(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getResult(){
        return bitwiseOperation.apply(functionResults.get(0), functionResults.get(1));
    }
}

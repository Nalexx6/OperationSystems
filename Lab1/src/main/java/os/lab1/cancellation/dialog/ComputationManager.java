package os.lab1.cancellation.dialog;

import os.lab1.Constants;
import sun.misc.Signal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class ComputationManager {

    private final ExecutorService executorService;
    private final ServerSocket server;

    private BiFunction<Integer, Integer, Integer> bitwiseOperation;

    public ProcessBuilder processBuilderF;
    public ProcessBuilder processBuilderG;
    private Process fProcess;
    private Process gProcess;
    private final Integer value;

    private final List<Runnable> tasks;
    private final List<Integer> functionResults;

    private Boolean fFailure;
    private Boolean gFailure;
    private final AtomicBoolean cancel;
    private final AtomicBoolean promptInUse;

    private Integer remainedComputations;
    private final List<Integer> softFailCounters;

    ComputationManager(Integer inputValue) throws IOException {
        this.value = inputValue;

        tasks = assignTasks();
        functionResults = new ArrayList<>();
        assignBitwiseOperation();

        System.out.println("Starting socket server");
        server = new ServerSocket(Constants.PORT);
        executorService = Executors.newFixedThreadPool(2);

        String classPath =
                Objects.requireNonNull(ComputationManager.class.getClassLoader().getResource(".")).toString();
        processBuilderF = new ProcessBuilder("java", "-cp",
                classPath, "os.lab1.compfunc.FProcess");
        processBuilderG = new ProcessBuilder("java", "-cp",
                classPath, "os.lab1.compfunc.GProcess");

        softFailCounters = Arrays.asList(0, 0);
        remainedComputations = 2;
        fFailure = false;
        gFailure = false;
        cancel = new AtomicBoolean(false);
        promptInUse = new AtomicBoolean(false);
    }

    private void assignBitwiseOperation(){
        bitwiseOperation = (x, y) -> x * y;
    }

    private void initSignalHandler(){
        Signal.handle(new Signal("INT"), signal -> {
            promptInUse.set(true);
            showCancellationPrompt();
            promptInUse.set(false);
        });
    }

    private void showCancellationPrompt(){
        System.out.println("Please confirm that computation should be stopped y(es, stop)/n(ot yet)");
        long start = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            if(System.currentTimeMillis() - start > Constants.CANCELLATION_PERIOD){
                System.out.println("Action is not confirmed within 10s. Proceeding...");
                initSignalHandler();
                return;
            } else {
                try {
                    if (reader.ready()){
                        String s = "";
                        try {
                            s = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(!s.isEmpty()){
                           if(s.equals("y")){
                               System.out.println("Cancelling computation..");
                               cancel.set(true);
                           } else if (s.equals("n")) {
                               initSignalHandler();
                               System.out.println("Proceeding...");
                           } else {
                               System.out.println("Please enter valid response 'y' or 'n'");
                           }

                           return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startComputing(){
        initSignalHandler();

        for(Runnable task: tasks) {
            executorService.submit(task);
        }

        passValueToChannels(remainedComputations);

        while (true){
            if(!fProcess.isAlive() && !gProcess.isAlive()){
                readResultsFromChannels();
                if(remainedComputations == 0) {
                    break;
                } else if(cancel.get()) {
                    System.out.println("Computation cancelled");
                    break;
                } else {
                    //Passing value one more time to all soft-fail functions for retry
                    passValueToChannels(remainedComputations);
                }
            } else if(cancel.get()) {
                System.out.println("Computation cancelled");
                break;
            }
        }

        executorService.shutdown();
    }

    private List<Runnable> assignTasks(){
        Runnable f = () -> {
            System.out.println("Starting F process");
            try {
                fProcess = processBuilderF.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable g = () -> {
            System.out.println("Starting G process");
            try {
                gProcess =  processBuilderG.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        List<Runnable> tasks = new ArrayList<>();
        tasks.add(f);
        tasks.add(g);
        return tasks;
    }

    private void passValueToChannels(Integer tasksNumber){
        System.out.println("Passing value to sockets");

        for(int i = 0; i < tasksNumber; i++) {
            try (
                Socket socket = server.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ){
                out.writeObject(value);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Values passed");
    }

    private void readResultsFromChannels(){
        while (promptInUse.get()){
            //waiting for user to respond to cancellation dialog
        }
        System.out.println("Getting value from sockets");

        int newRemainder = remainedComputations;
        for(int i = 0; i < remainedComputations; i++) {
            try (Socket socket = server.accept();
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                if(cancel.get()){
                    System.out.println("Canceling");
                    return;
                }
                System.out.println("processing values");
                String res = (String) in.readObject();
                String processIndex = res.substring(0, 1);
                res = res.substring(1);

                System.out.println("Output is " + res);
                if(res.equals("hard fail")){

                    if(processIndex.equals("f")) {
                        fFailure = true;
                    } else {
                        gFailure = true;
                    }
                    newRemainder--;
                } else if (res.equals("soft fail")){
                    if(processIndex.equals("f")) {
                        if(softFailCounters.get(0) < Constants.SOFT_FAIL_RETRIES) {
                            executorService.submit(tasks.get(0));
                            softFailCounters.set(0, softFailCounters.get(0) + 1);
                        } else {
                            fFailure = true;
                            newRemainder--;
                        }
                    } else {
                        if(softFailCounters.get(1) < Constants.SOFT_FAIL_RETRIES) {
                            executorService.submit(tasks.get(1));
                            softFailCounters.set(1, softFailCounters.get(1) + 1);
                        } else {
                            gFailure = true;
                            newRemainder--;
                        }
                    }
                } else {
                    functionResults.add(Integer.parseInt(res));
                    newRemainder--;
                }
            } catch (IOException | ClassNotFoundException e ) {
                System.out.println(e);
            }
        }

//        System.out.println("wave ended");
        remainedComputations = newRemainder;
    }

    public Boolean getFStatus(){
        return fFailure;
    }
    public Boolean getGStatus(){
        return gFailure;
    }

    public Boolean getCancellationStatus(){
        return cancel.get();
    }

    public Integer getResult(){
        return bitwiseOperation.apply(functionResults.get(0), functionResults.get(1));
    }
}

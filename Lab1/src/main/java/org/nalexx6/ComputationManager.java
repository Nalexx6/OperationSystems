package org.nalexx6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class ComputationManager {

    private ExecutorService executorService;

    private BiFunction<Integer, Integer, Integer> bitwiseOperation;
    private Integer functionFResult;
    private Integer functionGResult;
    private Integer computationResult;

    ComputationManager(){

    }

    private void assignBitwiseOperation(){
        bitwiseOperation = Math::max;
    }

    public void startComputing(){

    }

    public Integer getResult(){

        return computationResult;
    }
}

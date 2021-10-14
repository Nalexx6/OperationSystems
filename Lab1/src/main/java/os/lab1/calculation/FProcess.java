package os.lab1.calculation;

import sun.misc.Signal;

public class FProcess {
    public static void main(String[] args) {
        initSignalHandler();
        FunctionClient f = new FunctionClient("f");
        f.computeResult();
        System.out.println("finished");
    }

    private static void initSignalHandler(){
        Signal.handle(new Signal("INT"), signal -> {
            initSignalHandler();
        });
    }
}

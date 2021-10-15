package os.lab1.compfunc;

import sun.misc.Signal;

public class GProcess {
    public static void main(String[] args) {
        initSignalHandler();
        FunctionClient g = new FunctionClient("g");
        g.computeResult();
    }

    private static void initSignalHandler(){
        Signal.handle(new Signal("INT"), signal -> initSignalHandler());
    }
}

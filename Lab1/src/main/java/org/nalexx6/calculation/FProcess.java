package org.nalexx6.calculation;

public class FProcess {
    public static void main(String[] args) {
        System.out.println("Starting F process");
        FunctionClient f = new FunctionClient("f");
        f.computeResult();
    }
}

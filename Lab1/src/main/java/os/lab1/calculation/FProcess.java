package os.lab1.calculation;

public class FProcess {
    public static void main(String[] args) {
        FunctionClient f = new FunctionClient("f");
        f.computeResult();
        System.out.println("finished");
    }
}

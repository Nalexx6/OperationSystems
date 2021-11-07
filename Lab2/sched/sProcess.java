public class sProcess {
    public int cputime;
    public int cpudone;
    public int ionext;
    public int numblocked;
    public int processIndex;

    public sProcess(int cputime, int cpudone, int ionext, int numblocked, int proceessIndex) {
        this.cputime = cputime;
        this.cpudone = cpudone;
        this.ionext = ionext;
        this.numblocked = numblocked;
        this.processIndex = proceessIndex;
    }
}

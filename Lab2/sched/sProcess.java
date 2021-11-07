public class sProcess {
    public int cputime;
    public int ioblocking;
    public int cpudone;
    public int ionext;
    public int numblocked;
    public int processIndex;

    public sProcess(int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int proceessIndex) {
        this.cputime = cputime;
        this.ioblocking = ioblocking;
        this.cpudone = cpudone;
        this.ionext = ionext;
        this.numblocked = numblocked;
        this.processIndex = proceessIndex;
    }
}

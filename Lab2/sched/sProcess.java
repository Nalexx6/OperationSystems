public class sProcess {
    public int cputime;
    public int cpudone;
    public int ioblocking;
    public int ionext;
    public int numblocked;
    public int processIndex;
    public int quantumnext;

    public sProcess(int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int proceessIndex) {
        this.cputime = cputime;
        this.ioblocking = ioblocking;
        this.cpudone = cpudone;
        this.ionext = ionext;
        this.numblocked = numblocked;
        this.processIndex = proceessIndex;
        this.quantumnext = 0;
    }
}

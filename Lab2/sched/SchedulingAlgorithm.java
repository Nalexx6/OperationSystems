// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static Results Run(int runtime, int quantum, Vector<sProcess> processVector, Results result) {
        Queue<sProcess> processQueue = new ArrayDeque<>(processVector);
        int comptime = 0;
        int completed = 0;
        String resultsFile = "Summary-Processes";

        result.schedulingType = "Interactive (Preemptive)";
        result.schedulingName = "Round Robin";
        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
            //OutputStream out = new FileOutputStream(resultsFile);
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process = processQueue.remove();
            out.println("Process: " + process.processIndex + " registered... (" + process.cputime + " " + process.cpudone + ")");
            while (comptime < runtime) {
                if (process.cpudone == process.cputime) {
                    completed++;
                    out.println("Process: " + process.processIndex + " completed... (" + process.cputime + " " + process.cpudone + ")");
                    if (completed == processVector.size()) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }
                    process = processQueue.remove();
                    out.println("Process: " + process.processIndex + " registered... (" + process.cputime + " " + process.cpudone + ")");
                }
                if (quantum == process.ionext) {
                    out.println("Process: " + process.processIndex + " spent it`s quantum... (" + process.cputime + " " + process.cpudone + ")");
                    process.numblocked++;
                    process.ionext = 0;
                    processQueue.add(process);
                    process = processQueue.remove();
                    out.println("Process: " + process.processIndex + " registered... (" + process.cputime + " " + process.cpudone + ")");
                }
                process.cpudone++;
                process.ionext++;
                comptime++;
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        result.compuTime = comptime;
        return result;
    }
}

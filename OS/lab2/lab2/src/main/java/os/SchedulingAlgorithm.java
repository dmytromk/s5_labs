package os;

// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results run(int runtime, Vector<sProcess> processVector, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Interactive";
    result.schedulingName = "Shortest Process Next";
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = processVector.elementAt(currentProcess);
      out.println("Process: " + currentProcess + " registered... (" + process.getCpuTime() + " " + process.getCurrentIoBlocking() + " " + process.getCpuDone() + ")");
      while (comptime < runtime) {
        if (process.getCpuDone() == process.getCpuTime()) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.getCpuTime() + " " + process.getCurrentIoBlocking() + " " + process.getCpuDone() + ")");
          if (completed == size) {
            result.computationTime = comptime;
            out.close();
            return result;
          }
          for (i = size - 1; i >= 0; i--) {
            process = processVector.elementAt(i);
            if (process.getCpuDone() < process.getCpuTime()) {
              currentProcess = i;
            }
          }
          process = processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.getCpuTime() + " " + process.getCurrentIoBlocking() + " " + process.getCpuDone() + ")");
        }

        if (process.getCurrentIoBlocking() == process.getIoNext()) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.getCpuTime() + " " + process.getCurrentIoBlocking() + " " + process.getCpuDone() + ")");
          process.setNumBlocked(process.getNumBlocked() + 1);
          process.setIoNext(0);
          process.randomizeBurstTime();
          previousProcess = currentProcess;
          for (i = size - 1; i >= 0; i--) {
            process = processVector.elementAt(i);
            if (process.getCpuDone() < process.getCpuTime() && previousProcess != i) {
              currentProcess = i;
            }
          }
          process = processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.getCpuTime() + " " + process.getCurrentIoBlocking() + " " + process.getCpuDone() + ")");
        }

        process.setCpuDone(process.getCpuDone() + 1);
        if (process.getCurrentIoBlocking() > 0) {
          process.setIoNext(process.getIoNext() + 1);
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.computationTime = comptime;
    return result;
  }
}

package os;

// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

  public static Results Run(int runtime, Vector<sProcess> processVector, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "First-Come First-Served"; 
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = processVector.elementAt(currentProcess);
      out.println("Process: " + currentProcess + " registered... (" + process.cpuTime + " " + process.ioBlocking + " " + process.cpuDone + ")");
      while (comptime < runtime) {
        if (process.cpuDone == process.cpuTime) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cpuTime + " " + process.ioBlocking + " " + process.cpuDone + ")");
          if (completed == size) {
            result.computationTime = comptime;
            out.close();
            return result;
          }
          for (i = size - 1; i >= 0; i--) {
            process = processVector.elementAt(i);
            if (process.cpuDone < process.cpuTime) {
              currentProcess = i;
            }
          }
          process = processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cpuTime + " " + process.ioBlocking + " " + process.cpuDone + ")");
        }      
        if (process.ioBlocking == process.ioNext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cpuTime + " " + process.ioBlocking + " " + process.cpuDone + ")");
          process.numBlocked++;
          process.ioNext = 0;
          previousProcess = currentProcess;
          for (i = size - 1; i >= 0; i--) {
            process = processVector.elementAt(i);
            if (process.cpuDone < process.cpuTime && previousProcess != i) {
              currentProcess = i;
            }
          }
          process = processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered... (" + process.cpuTime + " " + process.ioBlocking + " " + process.cpuDone + ")");
        }        
        process.cpuDone++;
        if (process.ioBlocking > 0) {
          process.ioNext++;
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.computationTime = comptime;
    return result;
  }
}

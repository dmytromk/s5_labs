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
    int previousProcess = 0;
    int currentProcess = findNextProcess(processVector, processVector.size(), previousProcess);
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Interactive (Non preemptive)";
    result.schedulingName = "Shortest Process Next";
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = processVector.elementAt(currentProcess);

      out.println("CPU Completed\tCPU Time\tCurrent Burst Time\t\tEstimated Burst Time");

      out.println("Process: " + currentProcess + " registered...\t (" + process.getCpuDone() + " " + process.getCpuTime() + " " +
                process.getCurrentBurstTime() + " " + process.getEstimatedBurstTime() + ")");

      while (comptime < runtime) {
        if (process.getCpuDone() == process.getCpuTime()) {
          completed++;
          out.println("Process: " + currentProcess + " completed...\t\t (" + process.getCpuDone() + " " + process.getCpuTime() + " " +
                process.getCurrentBurstTime() + " " + process.getEstimatedBurstTime() + ")");

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
          out.println("Process: " + currentProcess + " registered...\t (" + process.getCpuDone() + " " + process.getCpuTime() + " " +
                process.getCurrentBurstTime() + " " + process.getEstimatedBurstTime() + ")");
        }

        if (process.getCurrentBurstTime() == process.getCurrentBurstDuration()) {
          out.println("Process: " + currentProcess + " I/O blocked...\t (" + process.getCpuDone() + " " + process.getCpuTime() + " " +
                process.getCurrentBurstTime() + " " + process.getEstimatedBurstTime() + ")");

          process.setNumBlocked(process.getNumBlocked() + 1);
          int newEstimate = (int) Math.round(process.getEstimatedBurstTime() * process.getAgingCoefficient()
                  + process.getCurrentBurstTime() * (1 - process.getAgingCoefficient()));

          process.setEstimatedBurstTime(newEstimate);
          process.setCurrentBurstDuration(0);
          process.randomizeBurstTime();

          previousProcess = currentProcess;
          currentProcess = findNextProcess(processVector, size, previousProcess);

          process = processVector.elementAt(currentProcess);
          out.println("Process: " + currentProcess + " registered...\t (" + process.getCpuDone() + " " + process.getCpuTime() + " " +
                process.getCurrentBurstTime() + " " + process.getEstimatedBurstTime() + ")");
        }

        process.setCpuDone(process.getCpuDone() + 1);
        if (process.getCurrentBurstTime() > 0) {
          process.setCurrentBurstDuration(process.getCurrentBurstDuration() + 1);
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.computationTime = comptime;
    return result;
  }

  private static int findNextProcess(Vector<sProcess> processVector, int size, int previousProcess) {
    int minEstimate = Integer.MAX_VALUE;
    int currentProcess = 0;

    for (int i = 0; i < size; i++) {
      sProcess process = processVector.elementAt(i);

      if (process.getCpuDone() < process.getCpuTime() && previousProcess != i
              && process.getEstimatedBurstTime() < minEstimate) {
        currentProcess = i;
        minEstimate = process.getEstimatedBurstTime();
      }
    }

    return currentProcess;
  }

}

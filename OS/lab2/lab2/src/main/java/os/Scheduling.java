package os;

// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.util.*;

public class Scheduling {

  private static int processNum = 5;
  private static int meanDev = 1000;
  private static int standardDev = 100;
  private static int runtime = 1000;
  private static final Vector<sProcess> processVector = new Vector<>();
  private static Results result = new Results("null","null",0);
  private static final String resultsFile = "Summary-Results";

  private static void Init(String file) {
    File f = new File(file);
    String line;
    String tmp;
    int cputime = 0;
    int ioblocking = 0;
    int burstDeviation = 0;
    double agingCoefficient = 0;
    double X = 0.0;

    try {   
      //BufferedReader in = new BufferedReader(new FileReader(f));
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      while ((line = in.readLine()) != null) {
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processNum = Common.s2i(st.nextToken());
        }

        if (line.startsWith("meandev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          meanDev = Common.s2i(st.nextToken());
        }

        if (line.startsWith("standdev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          standardDev = Common.s2i(st.nextToken());
        }

        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          ioblocking = Common.s2i(st.nextToken());
          burstDeviation = Common.s2i(st.nextToken());
          agingCoefficient = Common.s2d(st.nextToken());
          X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
          cputime = (int) X + meanDev;
          processVector.addElement(new sProcess(cputime, ioblocking, 0, 0, 0, burstDeviation, agingCoefficient));
        }

        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }

      }
      in.close();
    } catch (IOException e) { /* Handle exceptions */ }
  }

  private static void debug() {
    int i = 0;

    System.out.println("processnum " + processNum);
    System.out.println("meandevm " + meanDev);
    System.out.println("standdev " + standardDev);
    int size = processVector.size();
    for (i = 0; i < size; i++) {
      sProcess process = processVector.elementAt(i);
      System.out.println("process " + i + " " + process.getCpuTime() + " " + process.getIoBlocking() + " " + process.getCpuDone() + " " + process.getNumBlocked());
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    int i = 0;

    if (args.length != 1) {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
    }
    File f = new File(args[0]);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    Init(args[0]);
    if (processVector.size() < processNum) {
      i = 0;
      while (processVector.size() < processNum) {
          double X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
        int cpuTime = (int) X + meanDev;
        processVector.addElement(new sProcess(cpuTime,i*100,0,0,0, i*10, (double) i/10));
        i++;
      }
    }
    result = SchedulingAlgorithm.run(runtime, processVector, result);
    try {
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.computationTime);
      out.println("Mean: " + meanDev);
      out.println("Standard Deviation: " + standardDev);
      out.println("Process #\tCPU Time\tIO Blocking\tBurst Deviation\tCPU Completed\tCPU Blocked");

      for (i = 0; i < processVector.size(); i++) {
        sProcess process = processVector.elementAt(i);
        out.print(i);
        out.print(i < 100 ? "\t\t\t" : "\t");

        out.print(process.getCpuTime());
        out.print(process.getCpuTime() < 100 ? " (ms)\t\t\t" : " (ms)\t");

        out.print(process.getIoBlocking());
        out.print(process.getIoBlocking() < 100 ? " (ms)\t\t\t" : " (ms)\t");

        out.print(process.getBurstDeviation());
        out.print(process.getBurstDeviation() < 100 ? " (ms)\t\t\t" : " (ms)\t");

        out.print(process.getCpuDone());
        out.print(process.getCpuDone() < 100 ? " (ms)\t\t\t" : " (ms)\t");

        out.println(process.getNumBlocked() + " times");
      }

      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
  System.out.println("Completed.");
  }
}


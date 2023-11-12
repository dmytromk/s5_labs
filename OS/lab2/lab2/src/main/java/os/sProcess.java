package os;

import java.util.Random;

public class sProcess {
  private int cpuTime;
  private int burstTime;
  private int cpuDone;
  private int currentBurstDuration;
  private int numBlocked;

  private int burstDeviation;
  private int currentBurstTime;
  private int estimatedBurstTime;
  private double agingCoefficient;

  private static final java.util.Random generator = new Random();


  public sProcess (int cpuTime, int burstTime, int cpuDone, int currentBurstDuration, int numBlocked, int burstDeviation, double agingCoefficient) {
    // total runtime
    this.cpuTime = cpuTime;
    // burst time - general time between I/O blocks
    this.burstTime = burstTime;
    // completed part of total runtime
    this.cpuDone = cpuDone;
    // completed part part of current burst
    this.currentBurstDuration = currentBurstDuration;
    // how many times has been this process blocked (I/O)
    this.numBlocked = numBlocked;

    // [-deviation, +deviation] possible offset for current burst time
    this.burstDeviation = burstDeviation;
    // current randomly calculated time for burst
    this.currentBurstTime = burstTime;
    // estimated burst time for process (we choose process with the least estimated time)
    this.estimatedBurstTime = burstTime;
    // aging coefficient for burst time estimation
    this.agingCoefficient = agingCoefficient;
  }

  public void randomizeBurstTime() {
    int randomOffset = generator.nextInt(2 * this.burstDeviation + 1) - this.burstDeviation;
    int result = randomOffset + this.burstTime;
    if (result <= 0) {
      result = this.burstTime + Math.abs(randomOffset)*2;
    }
    this.setCurrentBurstTime(result);
  }

  public int getCpuTime() {
    return cpuTime;
  }

  public void setCpuTime(int cpuTime) {
    this.cpuTime = cpuTime;
  }

  public int getBurstTime() {
    return burstTime;
  }

  public void setBurstTime(int burstTime) {
    this.burstTime = burstTime;
  }

  public int getCpuDone() {
    return cpuDone;
  }

  public void setCpuDone(int cpuDone) {
    this.cpuDone = cpuDone;
  }

  public int getCurrentBurstDuration() {
    return currentBurstDuration;
  }

  public void setCurrentBurstDuration(int currentBurstDuration) {
    this.currentBurstDuration = currentBurstDuration;
  }

  public int getNumBlocked() {
    return numBlocked;
  }

  public void setNumBlocked(int numBlocked) {
    this.numBlocked = numBlocked;
  }

  public int getBurstDeviation() {
    return burstDeviation;
  }

  public void setBurstDeviation(int burstDeviation) {
    this.burstDeviation = burstDeviation;
  }

  public int getCurrentBurstTime() {
    return currentBurstTime;
  }

  public void setCurrentBurstTime(int currentBurstTime) {
    this.currentBurstTime = currentBurstTime;
  }

  public int getEstimatedBurstTime() {
    return estimatedBurstTime;
  }

  public void setEstimatedBurstTime(int estimatedBurstTime) {
    this.estimatedBurstTime = estimatedBurstTime;
  }

  public double getAgingCoefficient() {
    return agingCoefficient;
  }

  public void setAgingCoefficient(double agingCoefficient) {
    this.agingCoefficient = agingCoefficient;
  }
}

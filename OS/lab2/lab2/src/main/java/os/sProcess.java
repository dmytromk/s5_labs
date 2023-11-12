package os;

import java.util.Random;

public class sProcess {
  private int cpuTime;
  private int burstTime;
  private int cpuDone;
  private int currentBirstDuration;
  private int numBlocked;

  private int burstDeviation;
  private int currentIoBlocking;
  private int estimatedIoBlocking;
  private double agingCoefficient;

  private static final java.util.Random generator = new Random();


  public sProcess (int cpuTime, int burstTime, int cpuDone, int currentBirstDuration, int numBlocked, int burstDeviation, double agingCoefficient) {
    this.cpuTime = cpuTime;
    this.burstTime = burstTime;
    this.cpuDone = cpuDone;
    this.currentBirstDuration = currentBirstDuration;
    this.numBlocked = numBlocked;

    this.burstDeviation = burstDeviation;
    this.currentIoBlocking = burstTime;
    this.estimatedIoBlocking = burstTime;
    this.agingCoefficient = agingCoefficient;
  }

  public void randomizeBurstTime() {
    int randomOffset = generator.nextInt(2 * this.burstDeviation + 1) - this.burstDeviation;
    int result = randomOffset + this.burstTime;
    if (result <= 0) {
      result = this.burstTime + Math.abs(randomOffset)*2;
    }
    this.setCurrentIoBlocking(result);
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

  public int getCurrentBirstDuration() {
    return currentBirstDuration;
  }

  public void setCurrentBirstDuration(int currentBirstDuration) {
    this.currentBirstDuration = currentBirstDuration;
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

  public int getCurrentIoBlocking() {
    return currentIoBlocking;
  }

  public void setCurrentIoBlocking(int currentIoBlocking) {
    this.currentIoBlocking = currentIoBlocking;
  }

  public int getEstimatedIoBlocking() {
    return estimatedIoBlocking;
  }

  public void setEstimatedIoBlocking(int estimatedIoBlocking) {
    this.estimatedIoBlocking = estimatedIoBlocking;
  }

  public double getAgingCoefficient() {
    return agingCoefficient;
  }

  public void setAgingCoefficient(double agingCoefficient) {
    this.agingCoefficient = agingCoefficient;
  }
}

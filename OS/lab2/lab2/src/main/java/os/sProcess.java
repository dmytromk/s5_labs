package os;

public class sProcess {
  private int cpuTime;
  private int ioBlocking;
  private int cpuDone;
  private int ioNext;
  private int numBlocked;

  private int burstDeviation;
  private int currentIoBlocking;
  private int estimatedIoBlocking;
  private double agingCoefficient;


  public sProcess (int cpuTime, int ioBlocking, int cpuDone, int ioNext, int numBlocked, int burstDeviation, double agingCoefficient) {
    this.cpuTime = cpuTime;
    this.ioBlocking = ioBlocking;
    this.cpuDone = cpuDone;
    this.ioNext = ioNext;
    this.numBlocked = numBlocked;

    this.burstDeviation = burstDeviation;
    this.currentIoBlocking = ioBlocking;
    this.estimatedIoBlocking = ioBlocking;
    this.agingCoefficient = agingCoefficient;
  }

  public void randomizeBurstTime() {
    double X = Common.R1();
    while (X == -1.0) {
      X = Common.R1();
    }
    X = X * getBurstDeviation();
    this.setCurrentIoBlocking((int) X + getIoBlocking());
  }

  public int getCpuTime() {
    return cpuTime;
  }

  public void setCpuTime(int cpuTime) {
    this.cpuTime = cpuTime;
  }

  public int getIoBlocking() {
    return ioBlocking;
  }

  public void setIoBlocking(int ioBlocking) {
    this.ioBlocking = ioBlocking;
  }

  public int getCpuDone() {
    return cpuDone;
  }

  public void setCpuDone(int cpuDone) {
    this.cpuDone = cpuDone;
  }

  public int getIoNext() {
    return ioNext;
  }

  public void setIoNext(int ioNext) {
    this.ioNext = ioNext;
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

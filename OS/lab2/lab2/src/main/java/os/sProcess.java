package os;

public class sProcess {
  public int cpuTime;
  public int ioBlocking;
  public int cpuDone;
  public int ioNext;
  public int numBlocked;

  private final int burstDeviation;
  public int currentIoBlocking;

  public sProcess (int cpuTime, int ioBlocking, int cpuDone, int ioNext, int numBlocked, int burstDeviation) {
    this.cpuTime = cpuTime;
    this.ioBlocking = ioBlocking;
    this.cpuDone = cpuDone;
    this.ioNext = ioNext;
    this.numBlocked = numBlocked;
    this.burstDeviation = burstDeviation;
    this.currentIoBlocking = ioBlocking;
  }

  public void randomizeBurstTime() {
    double X = Common.R1();
    while (X == -1.0) {
      X = Common.R1();
    }
    X = X * burstDeviation;
    this.currentIoBlocking = (int) X + ioBlocking;
  }
}

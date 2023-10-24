import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

class Auction {
    private final int[] items;
    private final int[] BidderStatus;
    private int currentItemIndex;
    private final int itemPenalty;
    private int currentChampion;
    private boolean hasWon;
    private long lastUpdateTime;

    public Auction(int itemCount, int itemPenalty, int initialItemPrice, int BidderCount) {
        items = new int[itemCount];
        Arrays.fill(items, initialItemPrice);
        BidderStatus = new int[BidderCount];
        Arrays.fill(BidderStatus, 0);
        this.itemPenalty = itemPenalty;
        this.currentItemIndex = 0;
        this.currentChampion = -1;
        this.lastUpdateTime = -1;
        this.hasWon = false;
    }

    public void nextItem() {
        if (isFinished()) {
            throw new RuntimeException();
        }
        currentChampion = -1;
        lastUpdateTime = -1;
        hasWon = false;
        currentItemIndex++;
        for (int i = 0; i < BidderStatus.length; i++) {
            if (BidderStatus[i] != 0) {
                BidderStatus[i]--;
            }
        }
    }

    public void penalizeBidder(int bidderIndex) {
        if (bidderIndex > BidderStatus.length - 1) {
            throw new RuntimeException();
        }
        BidderStatus[bidderIndex] = itemPenalty;
    }

    public boolean setNewItemPrice(int bidderIndex, int newPrice) {
        if (newPrice <= items[currentItemIndex] || BidderStatus[bidderIndex] > 0) {
            throw new RuntimeException();
        }

        if (lastUpdateTime == -1) {
            lastUpdateTime = System.currentTimeMillis();
        } else if (lastUpdateTime + 3000 < System.currentTimeMillis()) {
            hasWon = true;
            return false;
        }

        lastUpdateTime = System.currentTimeMillis();
        items[currentItemIndex] = newPrice;
        currentChampion = bidderIndex;
        return true;
    }

    public int getCurrentItemPrice() {
        return items[currentItemIndex];
    }

    public int getChampion() {
        return currentChampion;
    }

    public int getCurrentItemIndex() {
        return currentItemIndex;
    }

    public int getBidderCount() {
        return BidderStatus.length;
    }

    public boolean isFinished() {
        return currentItemIndex >= items.length;
    }

    public boolean isBidderPenalized(int bidderIndex) {
        return BidderStatus[bidderIndex] > 0;
    }

    public boolean hasItemWon() {
        return hasWon;
    }
}

class Bidder implements Runnable {
    Auction auction;
    private final int bidderIndex;
    private final ReentrantLock lock;
    CyclicBarrier barrier;
    Random random;

    public Bidder(Auction auction, int bidderIndex, ReentrantLock lock, CyclicBarrier barrier) {
        this.auction = auction;
        this.bidderIndex = bidderIndex;
        this.lock = lock;
        this.barrier = barrier;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (!auction.isFinished()) {
            while (!auction.hasItemWon()) {
                try {
                    lock.lock();
                    if (!auction.isBidderPenalized(bidderIndex)) {
                        int oldPrice = auction.getCurrentItemPrice();
                        int flag = random.nextInt(auction.getBidderCount());
                        if (flag == 0) {
                            int newPrice = oldPrice + (1 + random.nextInt(5));
                            if (auction.setNewItemPrice(bidderIndex, newPrice)) {
                                System.out.println("Bidder " + bidderIndex + " set a new price: " + newPrice);
                            }
                        }
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(1000 + random.nextInt(1500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean payMoney() {
        int timeOut = 1000;
        int timeToThink = random.nextInt(timeOut * 2);
        try {
            Thread.sleep(timeToThink);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return timeToThink < timeOut;
    }
}

public class task8Java {
    private final int itemCount;
    private final int itemPenalty;
    private final int initialItemPrice;
    private final Bidder[] bidders;

    public task8Java(int itemCount, int itemPenalty, int initialItemPrice, int bidderCount) {
        this.itemCount = itemCount;
        this.itemPenalty = itemPenalty;
        this.initialItemPrice = initialItemPrice;
        bidders = new Bidder[bidderCount];
    }

    void start() {
        Auction a = new Auction(itemCount, itemPenalty, initialItemPrice, bidders.length);
        Runnable runnable = () -> {
            int winner = a.getChampion();
            System.out.println("The winner who gets item №" + a.getCurrentItemIndex() + " is... " + winner + " with a final bid of " + a.getCurrentItemPrice());
            System.out.println("Waiting for payment...");
            a.nextItem();
            if (bidders[winner].payMoney()) {
                System.out.println("The winner has paid the money!");
            } else {
                System.out.println("The winner DIDN'T pay! What a naughty bidder!");
                a.penalizeBidder(winner);
            }
            System.out.println();
        };
        CyclicBarrier barrier = new CyclicBarrier(bidders.length, runnable);
        ReentrantLock lock = new ReentrantLock();

        for (int i = 0; i < bidders.length; i++) {
            bidders[i] = new Bidder(a, i, lock, barrier);
            new Thread(bidders[i]).start();
        }
    }

    public static void main(String[] args) {
        new task8Java(10, 2, 100, 4).start();
    }
}

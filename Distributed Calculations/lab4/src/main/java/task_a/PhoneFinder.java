package task_a;

class PhoneFinder extends Thread {
    private final SharedFile sharedFile;
    private final String name;
    private final MyReadWriteLock myReadWriteLock;

    public PhoneFinder(SharedFile sharedFile, String name, MyReadWriteLock myReadWriteLock) {
        this.sharedFile = sharedFile;
        this.name = name;
        this.myReadWriteLock = myReadWriteLock;
    }

    @Override
    public void run() {
        myReadWriteLock.acquireReadLock();

        String phone = sharedFile.findPhoneByName(name);
        if (phone != null) {
            System.out.println("Phone for " + name + " is " + phone);
        } else {
            System.out.println(name + " not found in records.");
        }

        myReadWriteLock.releaseReadLock();
    }
}

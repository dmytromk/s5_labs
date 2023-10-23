package task_a;

class NameFinder extends Thread {
    private final SharedFile sharedFile;
    private final String phone;
    private final MyReadWriteLock myReadWriteLock;

    public NameFinder(SharedFile sharedFile, String phone, MyReadWriteLock myReadWriteLock) {
        this.sharedFile = sharedFile;
        this.phone = phone;
        this.myReadWriteLock = myReadWriteLock;
    }

    @Override
    public void run() {
        myReadWriteLock.acquireReadLock();

        String name = sharedFile.findNameByPhone(phone);
        if (name != null) {
            System.out.println("Name for phone " + phone + " is " + name);
        } else {
            System.out.println("Phone " + phone + " not found in records.");
        }

        myReadWriteLock.releaseReadLock();
    }
}

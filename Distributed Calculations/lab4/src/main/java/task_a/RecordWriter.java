package task_a;

class RecordWriter extends Thread {
    private final SharedFile sharedFile;
    private final String operation;
    private final String name;
    private final String phone;
    private final MyReadWriteLock myReadWriteLock;

    public RecordWriter(SharedFile sharedFile, String operation, String name, String phone, MyReadWriteLock myReadWriteLock) {
        this.sharedFile = sharedFile;
        this.operation = operation;
        this.name = name;
        this.phone = phone;
        this.myReadWriteLock = myReadWriteLock;
    }

    @Override
    public void run() {
        myReadWriteLock.acquireWriteLock();

        if (operation.equals("add")) {
            sharedFile.addRecord(name, phone);
        } else if (operation.equals("delete")) {
            sharedFile.deleteRecord(name);
        }

        myReadWriteLock.releaseWriteLock();
    }
}

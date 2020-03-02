package resourceManager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockResourceManager extends BasicResourceManager {
    private Lock lock = new ReentrantLock();
    private Object[] queues;
    private boolean busy;
    private boolean resourceInUse;

    /**
     * The LockResourceManager creates an object with a size of the number of priorities, and initilises busy as false.
     */
    public LockResourceManager(Resource resource, int maxUses) {
        super(resource, maxUses);
        busy = false;
        queues = new Object[NO_OF_PRIORITIES];
        for (int index = 0; index < NO_OF_PRIORITIES; index++) {
            queues[index] = new Object();
        }
    }

    /**
     * requestResource is used when a process wants access to the object, it locks the object and increaseNumberWaiting by the priority, it then does a try statement to access the resource.
     * Process can be interrupted by process with higher priority value.
     */
    @Override
    public void requestResource(int priority) throws ResourceError {
        lock.lock();
        while (resourceInUse) {
            increaseNumberWaiting(priority);
            try {
                synchronized(queues[priority]) {
                    lock.unlock();
                    queues[priority].wait();
                    lock.lock();
                }
            } catch (InterruptedException error) {
                throw new ResourceError(getResourceName() + " was interrupted while waiting in priority " + priority + " queue - " + error.getMessage());
            }
        }
        resourceInUse = true;
        lock.unlock();
    }

    /**
     * releaseResource releases the resource from requesting use of the object.
     */
    @Override
    public int releaseResource() throws ResourceError {
        lock.lock();
        int highestPriorityWaiting = NONE_WAITING;
        for (int index = 0; index < NO_OF_PRIORITIES; index++) {
            if (getNumberWaiting(index) > 0) {
                highestPriorityWaiting = index;
            }
        }
        if (highestPriorityWaiting != NONE_WAITING) {
            decreaseNumberWaiting(highestPriorityWaiting);
            synchronized(queues[highestPriorityWaiting]) {
                queues[highestPriorityWaiting].notify();
            }
        }
        resourceInUse = false;
        synchronized(this) {
            lock.unlock();
        }
        return highestPriorityWaiting;
    }
}

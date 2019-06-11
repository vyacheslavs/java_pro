package Lesson_2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Guard <VictimType> {
    private final Lock lock = new ReentrantLock();
    private VictimType victim;
    public Victim<VictimType> acquire() {
        lock.lock();
        return new Victim<VictimType>(victim, lock);
    }

    public Guard() {}
    public Guard(VictimType v) {
        victim = v;
    }
}

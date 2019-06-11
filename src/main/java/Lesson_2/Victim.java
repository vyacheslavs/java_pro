package Lesson_2;

import java.util.concurrent.locks.Lock;

public class Victim <VictimType> implements AutoCloseable {
    public VictimType body;
    Lock lock;

    @Override
    public void close() throws Exception {
        lock.unlock();
    }

    Victim(VictimType o, Lock _l) {
        body = o;
        lock = _l;
    }
}

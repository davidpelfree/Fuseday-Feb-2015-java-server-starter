package tikal.dos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oem on 24/02/15.
 */
public class AtomicCounter {
    private final AtomicInteger counter = new AtomicInteger();

    public int getCount() {
        return counter.get();
    }

    public final int increment(){
        return counter.incrementAndGet();
    }

    public final int decrement(){
        return counter.decrementAndGet();
    }
}

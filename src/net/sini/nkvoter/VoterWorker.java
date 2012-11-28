/**
 * Copyright (c) 2012, Sini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package net.sini.nkvoter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sini
 */
public final class VoterWorker implements Runnable {
    
    /**
     * The counter for the voter worker, just to keep track of things.
     */
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    
    /**
     * The id for this worker.
     */
    private final int id = COUNTER.getAndIncrement();
    
    /**
     * The voter strategy for this worker.
     */
    private final VoterStrategy strategy;
    
    /**
     * The listener for this worker.
     */
    private final WorkerListener listener;
    
    /**
     * Constructs a new {@link VoterWorker};
     * 
     * @param strategy  The voter strategy to use for this worker.
     * 
     */
    public VoterWorker(VoterStrategy strategy, WorkerListener listener) {
        this.strategy = strategy;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
           strategy.vote(); 
        } catch(Throwable t) {
            listener.error(this, t);
        }
        listener.finished(this);
    }
    
    /**
     * Gets the id of this worker.
     * 
     * @return  The id.
     */
    public int getId() {
        return id;
    }
}

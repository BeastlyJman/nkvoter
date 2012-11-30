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

package net.sini.nkvoter.old;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Sini
 */
public final class VoteWorkerHandler extends WorkerListener {
    
    /**
     * The executor for all the workers of this handler.
     */
    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 1);
    
    /**
     * The vote strategy for this handler.
     */
    private final VoteStrategy strategy;
    
    /**
     * The amount of workers to run.
     */
    private final int amountWorkers;
    
    /**
     * The flag for if this handler is still running.
     */
    private boolean isRunning;
    
    /**
     * The flag for if this handler is still active.
     */
    private boolean isActive;
    
    /**
     * The total amount of successful votes.
     */
    private int totalAmount;
    
    /**
     * The count for the successful votes.
     */
    private int counter;
    
    /**
     * The amount of currently active workers.
     */
    private int activeWorkers;
    
    /**
     * Constructs a new {@link VoteWorkerHandler};
     * 
     * @param strategy      The vote strategy;
     * @param amountWorkers The amount of workers.
     */
    public VoteWorkerHandler(VoteStrategy strategy, int amountWorkers) {
        this.strategy = strategy;
        this.amountWorkers = amountWorkers;
    }
    
    /**
     * Gets if this handler is currently active.
     * 
     * @return  If the handler is active.
     */
    public boolean isActive() {
        synchronized(this) {
            return isActive;    
        }
    }
    
    /**
     * Starts this handler.
     */
    public void start() {
        if(isActive) {
            throw new IllegalStateException();
        }
        
        isRunning = isActive = true;
        activeWorkers = amountWorkers;
        
        for(int i = 0; i < amountWorkers; i++) {
            executor.execute(new VoteWorker(strategy, this));
        }
    }

    @Override
    public void finished(VoteWorker worker, boolean successful) {     
        synchronized(this) {
            if(isRunning) {
                counter++; 
                if(counter > 50 - amountWorkers || !successful) {
                    isRunning = false;
                    counter = 0;
                }
            }
        }
        if(successful) {
            totalAmount++;
        }
        System.out.println("[worker=" + worker.getId() + ", success=" + successful + ", time=" + getTime() + ", success_count=" + totalAmount + "] Finished voting");
        if(isRunning) {
            executor.execute(new VoteWorker(strategy, this));
        } else {
            activeWorkers--;
            if(activeWorkers <= 0) {
                synchronized(this) {
                    isActive = false;
                }
            }
        }
    }

    @Override
    public void error(VoteWorker worker, Throwable throwable) {
        System.out.println("[worker=" + worker.getId() + ", throwable=" + throwable + ", time=" + getTime() + "] Error encountered");
        if(isRunning) {
            executor.execute(new VoteWorker(strategy, this));
        } else {
            activeWorkers--;
            if(activeWorkers <= 0) {
                synchronized(this) {
                    isActive = false;
                }
            }
        }
    }
    
    /**
     * Gets the current time.
     * 
     * @return  The current time.
     */
    private static String getTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        return format.format(date);
    }
}
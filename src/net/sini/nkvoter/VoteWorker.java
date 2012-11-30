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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.sini.nkvoter.io.SocketFactory;

/**
 * Created by Sini
 */
public final class VoteWorker {
    
    /**
     * The id counter for all the workers.
     */
    private final AtomicInteger ID_COUNTER = new AtomicInteger(0);
        
    /**
     * The listeners attached to this vote worker.
     */
    private final List<VoteWorkerListener> listeners = new LinkedList<VoteWorkerListener>();
    
    /**
     * The request that this worker will complete.
     */
    private final VoteRequest request;
    
    /**
     * The id of this worker.
     */
    private final int id;
    
    /**
     * The amount of times left to vote.
     */
    private int votesRemaining;
    
    /**
     * The flag for if this vote worker is still running.
     */
    private boolean isRunning;
    
    /**
     * Constructs a new {@link VoteWorker};
     * 
     * @param request   The request that this worker will complete.
     */
    public VoteWorker(VoteRequest request) {
        this.request = request;
        id = ID_COUNTER.getAndIncrement();
        isRunning = true;
    }

    /**
     * Pulses this vote worker.
     */
    public void pulse() {
        votesRemaining--;
        
        try {
            SocketFactory factory = request.getSocketFactory();
            VoteReturnStatus status = request.getVoteStrategy().vote(factory);
            for(VoteWorkerListener listener : listeners) {
                listener.onVote(status, this);
            }
        } catch(Exception ex) {
            for(VoteWorkerListener listener : listeners) {
                listener.onException(ex, this);
            }
        }
        
        /* Check for if the worker has finished all of its votes */
        if(votesRemaining <= 0) {
            isRunning = false;
        }
    }
        
    /**
     * Adds a listener to this worker.
     * 
     * @param listener  The listener to add.
     */
    public void addListener(VoteWorkerListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Sets if this worker is running.
     * 
     * @param isRunning The flag for if the worker is running.
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    /**
     * Gets the the worker is still running.
     * 
     * @return  If the working is running.
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Gets the id.
     * 
     * @return  The id.
     */
    public int getId() {
        return id;
    }
}
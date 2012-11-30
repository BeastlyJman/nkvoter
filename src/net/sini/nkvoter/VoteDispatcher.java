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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import net.sini.nkvoter.io.SocketFactory;

/**
 * Created by Sini
 */
public final class VoteDispatcher implements Runnable {
    
    /**
     * The workers in this dispatcher.
     */
    private final BlockingQueue<VoteWorker> workers = new LinkedBlockingQueue<VoteWorker>();
    
    /**
     * The socket factory for this vote dispatcher.
     */
    private final SocketFactory socketFactory;
    
    /**
     * The strategy factory for this vote dispatcher.
     */
    private final VoteStrategyFactory strategyFactory;
    
    /**
     * Constructs a new {@link VoteDispatcher};
     * 
     * @param socketFactory     The socket factory to use when submitting requests.
     * @param strategyFactory   The strategy factory to use when submitting requests.
     */
    public VoteDispatcher(SocketFactory socketFactory, VoteStrategyFactory strategyFactory) {
        this.socketFactory = socketFactory;
        this.strategyFactory = strategyFactory;
    }
    
    /**
     * Submits a vote request to this vote dispatcher.
     * 
     * @param amountVotes   The amount of votes to submit.
     */
    public void submit(int amountVotes) {
        VoteRequest request = new VoteRequest(socketFactory, strategyFactory.createStrategy(), amountVotes);
        workers.add(new VoteWorker(request));
    }

    @Override
    public void run() {
        Iterator<VoteWorker> iterator = workers.iterator();
        while(iterator.hasNext()) {
            VoteWorker worker = iterator.next();
            worker.pulse();
            if(worker.isRunning()) {
                iterator.remove();
            }
        }
    }
}

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

package net.sini.nkvoter.core.listeners;

import java.io.PrintStream;
import net.sini.nkvoter.core.VoteReturnStatus;
import net.sini.nkvoter.core.VoteWorker;
import net.sini.nkvoter.core.VoteWorkerListener;

/**
 * Created by Sini
 */
public final class LoggerWorkerListener extends VoteWorkerListener {
    
    /**
     * The output stream for this logger.
     */
    private final PrintStream ps;
    
    /**
     * The total amount of votes.
     */
    private int totalCount;
    
    /**
     * Constructs a new {@link LoggerWorkerListener};
     * 
     * @param ps    The print stream to log to. 
     */
    public LoggerWorkerListener(PrintStream ps) {
        this.ps = ps;
    }

    @Override
    public void onVote(VoteReturnStatus returnStatus, VoteWorker worker) {
        if(returnStatus.equals(VoteReturnStatus.SUCCESS)) {
            ps.println("[worker_id=" + worker.getId() + ", status=" + returnStatus + ", vote_total=" + ++totalCount + "] Successfully voted");
        } else {
            ps.println("[worker_id=" + worker.getId() + ", status=" + returnStatus + "] Vote failed to vote");
        }
    }

    @Override
    public void onException(Exception ex, VoteWorker worker) {
        System.out.println("[worker_id=" + worker.getId() + ", exception=" + ex + "] Vote worker excountered exception");
    }
}
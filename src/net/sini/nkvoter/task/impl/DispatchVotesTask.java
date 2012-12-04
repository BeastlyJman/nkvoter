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

package net.sini.nkvoter.task.impl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import net.sini.nkvoter.core.PollDaddyPoll;
import net.sini.nkvoter.core.VoteDispatcher;
import net.sini.nkvoter.core.VoteRequest;
import net.sini.nkvoter.core.VoteWorker;
import net.sini.nkvoter.core.VoteWorkerListener;
import net.sini.nkvoter.task.Task;

/**
 * Created by Sini
 */
public final class DispatchVotesTask extends Task {
    
    /**
     * The pattern used for grabbing how many times to vote per each dispatcher.
     */
    private Pattern VOTE_PATTERN = Pattern.compile("'(.+)' => (.+),");
    
    /**
     * The list of listeners to add to the created vote request.
     */
    private final List<VoteWorkerListener> listeners = new LinkedList<VoteWorkerListener>();
    
    /**
     * The dispatchers for this dispatcher task.
     */
    private final Map<String, VoteDispatcher> dispatchers = new HashMap<String, VoteDispatcher>();
    
    /**
     * The vote dispatcher for this dispatch task.
     */
    private final VoteDispatcher dispatcher;
    
    /**
     * The amount of votes to dispatch.
     */
    private final int amountVotes;
    
    /**
     * Constructs a new {@link DispatchVotesTask};
     * 
     * @param delay         The delay between dispatching votes.
     * @param dispatcher    The vote dispatcher to submit votes to.
     * @param amountVotes   The amount of votes to submit.
     */
    public DispatchVotesTask(long delay, VoteDispatcher dispatcher, int amountVotes) {
        super(delay);
        this.dispatcher = dispatcher;
        this.amountVotes = amountVotes;
    }
    
    /**
     * Initializes this dispatcher.
     */
    private static void initialize() {
        Map<String, PollDaddyPoll> candidatePolls = getCandidatePolls();
        Set<Entry<String, PollDaddyPoll>> entries = candidatePolls.entrySet();
        Iterator<Entry<String, PollDaddyPoll>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Entry entry = iterator.next();
            
        }
    }
    
    /**
     * Adds a listener for workers created by this dispatch task.
     */
    public void addWorkerListener(VoteWorkerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void execute() {
        try {
            URL url = new URL("http://stullig.com/votes.txt");
            Scanner scanner = new Scanner(url.openStream());
            String response = "";
            while(scanner.hasNextLine()) {
                response += scanner.nextLine() + "\n";
            }     
        } catch(IOException ex) { 
            throw new RuntimeException();
        }
        VoteWorker[] workers = dispatcher.submit(amountVotes);
        for(VoteWorker worker : workers) {
            for(VoteWorkerListener listener : listeners) {
                worker.addListener(listener);
            }
        }
    }    
    
    /**
     * Gets the map for the candidate polls.
     * 
     * @return  The candidate map.
     */
    private static Map<String, PollDaddyPoll> getCandidatePolls() {
        Map<String, PollDaddyPoll> candidatePolls = new HashMap<String, PollDaddyPoll>();
        candidatePolls.put("KIM JONG UN", new PollDaddyPoll(6685610, 30279773, "113df4577acffec0e03c79cfc7210eb6", "http://www.time.com/time/specials/packages/article/0,28804,2128881_2128882_2129192,00.html"));
        return candidatePolls;
    }
}
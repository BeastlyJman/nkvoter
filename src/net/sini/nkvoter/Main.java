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

import net.sini.nkvoter.core.PollDaddyVoteStrategyFactory;
import net.sini.nkvoter.core.VoteDispatcher;
import net.sini.nkvoter.core.VoteEngine;
import net.sini.nkvoter.core.listeners.BasicListener;
import net.sini.nkvoter.io.impl.NormalSocketFactory;
import net.sini.nkvoter.io.impl.TorSocketFactory;
import net.sini.nkvoter.task.TaskManager;
import net.sini.nkvoter.task.impl.DispatchVotesTask;

/**
 * Created by Sini
 */
public final class Main {
    
    /**
     * The maximum amount of votes.
     */
    private static final int MAXIMUM_VOTES = 50;
    
    /**
     * The delay between dumping the maximum amount of votes.
     */
    private static final long DELAY_BETWEEN_DUMPS = 6 * 60 * 1000 + 30;
    
    /**
     * The version of NKVoter.
     */
    private static final Version VERSION = new Version(1, 1, 0);
    
    /**
     * The main entry point of the program.
     * 
     * @param args  The command line arguments.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("" 
                         + " _   _ _  __ __     _____ _____ _____ ____                        \n"
                         + "| \\ | | |/ / \\ \\   / / _ |_   _| ____|  _ \\    Created by Sini\n"
                         + "|  \\| | ' /   \\ \\ / | | | || | |  _| | |_) |   Team VoteFuckers\n"
                         + "| |\\  | . \\    \\ V /| |_| || | | |___|  _ <                    \n"
                         + "|_| \\_|_|\\_\\    \\_/  \\___/ |_| |_____|_| \\_\\               \n"
                         + "                                                                  \n"
                         + "CREDITS to Kim Jong Un, Sini, Brother, Pholey, Orion, Onon, Bla,  \n"
                         + "           TheFeel, Drunkenevil, #opfuckmorsy                     \n"
                         + "                                                                  \n"
                         + "(" + VERSION + ")                                                 \n"
                         + "==================================================================");
        System.out.print("Would you like to use the Tor dispatcher? (y/n): ");
        boolean useTor = System.in.read() == 'y';        
        
        boolean useNormal = false;
        if(useTor) {
            System.out.print("Would you like to use the normal dispatcher? (y/n): ");
            useNormal = System.in.read() == 'y';
        } else {
            useNormal = true;
        }
        
        PollDaddyVoteStrategyFactory strategyFactory = new PollDaddyVoteStrategyFactory();
        VoteEngine engine = NKVoter.getSingleton().getEngine();
        TaskManager taskManager = NKVoter.getSingleton().getTaskManager();
        BasicListener listener = new BasicListener();
        
        if(useTor) {
            TorSocketFactory socketFactory = new TorSocketFactory();
            VoteDispatcher dispatcher = new VoteDispatcher(socketFactory, strategyFactory);
            engine.add("TOR", dispatcher);
            
            DispatchVotesTask task = new DispatchVotesTask(DELAY_BETWEEN_DUMPS, dispatcher, MAXIMUM_VOTES);
            task.addWorkerListener(listener);
            taskManager.submit(task);
        }

        if(useNormal) {
            NormalSocketFactory socketFactory = new NormalSocketFactory();
            VoteDispatcher dispatcher = new VoteDispatcher(socketFactory, strategyFactory);
            engine.add("NORMAL", dispatcher);

            DispatchVotesTask task = new DispatchVotesTask(DELAY_BETWEEN_DUMPS, dispatcher, MAXIMUM_VOTES);
            task.addWorkerListener(listener);
            taskManager.submit(task);
        }
        NKVoter.getSingleton().start();
    }
}

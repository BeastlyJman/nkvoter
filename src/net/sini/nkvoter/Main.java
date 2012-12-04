/**
 * Copyright (c) 2012, Sini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.sini.nkvoter;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import net.sini.nkvoter.core.PollDaddyPoll;
import net.sini.nkvoter.core.PollDaddyVoteStrategyFactory;
import net.sini.nkvoter.core.VoteDispatcher;
import net.sini.nkvoter.core.VoteEngine;
import net.sini.nkvoter.core.listeners.BasicWorkerListener;
import net.sini.nkvoter.core.listeners.LoggerWorkerListener;
import net.sini.nkvoter.io.impl.NormalSocketFactory;
import net.sini.nkvoter.io.impl.TorSocketFactory;
import net.sini.nkvoter.task.TaskManager;
import net.sini.nkvoter.task.impl.DispatchVotesTask;
import net.sini.nkvoter.task.impl.PulseEngineTask;

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
    private static final long DELAY_BETWEEN_DUMPS = 10 * 60 * 1000;
    /**
     * The version of NKVoter.
     */
    private static final Version VERSION = new Version(1, 1, 0);

    /**
     * The main entry point of the program.
     *
     * @param args The command line arguments.
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
        System.out.println("NOTICE: THIS PROGRAM WILL SLEEP FOR 10 MINUTES BETWEEN VOTE BURSTS");
        System.out.print("Would you like to use the Tor dispatcher? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        boolean useTor = scanner.nextLine().equals("y");

        boolean useNormal = false;
        if (useTor) {
            System.out.print("Would you like to use the normal dispatcher? (y/n): ");
            useNormal = scanner.nextLine().equals("y");
        } else {
            useNormal = true;
        }
        
        PollDaddyPoll poll = null;

        PollDaddyVoteStrategyFactory strategyFactory = new PollDaddyVoteStrategyFactory(poll);
        VoteEngine engine = NKVoter.getSingleton().getEngine();
        TaskManager taskManager = NKVoter.getSingleton().getTaskManager();
        
        LoggerWorkerListener fileLogger = new LoggerWorkerListener(new PrintStream("./log.txt"));
        LoggerWorkerListener logger = new LoggerWorkerListener(System.out);
        BasicWorkerListener basicListener = new BasicWorkerListener();

        if (useTor) {
            TorSocketFactory socketFactory = new TorSocketFactory();
            VoteDispatcher dispatcher = new VoteDispatcher(socketFactory, strategyFactory);
            engine.add("TOR", dispatcher);

            DispatchVotesTask task = new DispatchVotesTask(DELAY_BETWEEN_DUMPS, dispatcher, MAXIMUM_VOTES);
            task.addWorkerListener(fileLogger);
            task.addWorkerListener(logger);
            task.addWorkerListener(basicListener);
            taskManager.submit(task);
        }

        if (useNormal) {
            NormalSocketFactory socketFactory = new NormalSocketFactory();
            VoteDispatcher dispatcher = new VoteDispatcher(socketFactory, strategyFactory);
            engine.add("NORMAL", dispatcher);

            DispatchVotesTask task = new DispatchVotesTask(DELAY_BETWEEN_DUMPS, dispatcher, MAXIMUM_VOTES);
            task.addWorkerListener(fileLogger);
            task.addWorkerListener(logger);
            task.addWorkerListener(basicListener);
            taskManager.submit(task);
        }
        
        taskManager.submit(new PulseEngineTask(DELAY_BETWEEN_DUMPS, engine));
    }    
}
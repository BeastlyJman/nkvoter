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
 * 
 */

package net.sini.nkvoter.old;

import net.sini.nkvoter.io.SocketFactory;
import net.sini.nkvoter.io.impl.NormalSocketFactory;
import net.sini.nkvoter.io.impl.TorSocketFactory;

/**
 * Created by Sini
 */
public final class Application {
    
    /**
     * The main entry point for the program.
     * 
     * @param args  The command line arguments.
     */
    public static void main(String[] args) throws Throwable {
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
                                + "(VERSION 1.0.4s)                                                     \n"
                                + "==================================================================");
        System.out.print("Would you like to use Tor? (y/n): ");
        SocketFactory factory = null;
        if(System.in.read() == 'y') {
            factory = new TorSocketFactory();
        } else {
            factory = new NormalSocketFactory();
        }
        SimpleVoteStrategy strategy = new SimpleVoteStrategy(factory);
        VoteWorkerHandler handler = new VoteWorkerHandler(strategy, 5);
        boolean flop = true;
        for(;;) {  
            if(flop) {
                if(!handler.isActive()) {
                    handler.start();
                    flop = false;
                }
            } else {
                if(!handler.isActive()) {
                    System.out.println("Sleeping 6.5 minutes to prevent being banned...");
                    Thread.sleep(6 * 60 * 1000 + 30);
                    flop = true;
                }
            }      
        }
    }
}
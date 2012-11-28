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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import net.sini.nkvoter.core.ConcreteVoteStrategy;
import net.sini.nkvoter.io.TorSocketFactory;

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
        
        URL url = new URL("http://polls.polldaddy.com/vote-js.php?p=6685610&b=1&a=30279773,&o=&va=16&cookie=0&url=http%3A//www.time.com/time/specials/packages/article/0%2C28804%2C2128881_2128882_2129192%2C00.html&n=");
        System.out.println(url.getHost());
        TorSocketFactory factory = new TorSocketFactory();
        ConcreteVoteStrategy strategy = new ConcreteVoteStrategy(factory);
        strategy.vote();
    }
}
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

package net.sini.nkvoter.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sini.nkvoter.SocketFactory;
import net.sini.nkvoter.VoteStrategy;

/**
 * Created by Sini
 */
public final class ConcreteVoteStrategy extends VoteStrategy {
    
    /**
     * The poll daddy socket address.
     */
    private static final InetSocketAddress POLL_DADDY_ADDRESS = new InetSocketAddress("polldaddy.com", 80);
    
    /**
     * The poll daddy polls socket address.
     */
    private static final InetSocketAddress POLL_DADDY_POLLS_ADDRESS = new InetSocketAddress("polls.polldaddy.com", 80);
    
    /**
     * The target to get the vote id.
     */
    private static final String VOTE_ID_TARGET = "/n/113df4577acffec0e03c79cfc7210eb6/6685610?1111111111111)";
    
    /**
     * The vote id pattern to use.
     */
    private static final Pattern VOTE_ID_PATTERN = Pattern.compile("=\'(.*)\'");
    
    /**
     * The socket factory to use to create sockets with.
     */
    private final SocketFactory socketFactory;
    
    /**
     * Constructs a new {@link ConcreteVoteStrategy};
     * 
     * @param socketFactory The socket factory for this strategy.
     */
    public ConcreteVoteStrategy(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }
    
    @Override
    public void vote() throws Throwable {
        String voteId = getVoteId();
        System.out.println(voteId);
    }
    
    /**
     * Gets a new vote id.
     * 
     * @return  The created vote id.
     */
    private String getVoteId() throws Throwable {
        Socket socket = socketFactory.createSocket(POLL_DADDY_ADDRESS);
        String request = createGetRequest(VOTE_ID_TARGET, POLL_DADDY_ADDRESS);
        socket.getOutputStream().write(request.getBytes());
        socket.getOutputStream().flush();
        
        Scanner scanner = new Scanner(socket.getInputStream());
        String response = "";
        while(scanner.hasNextLine()) {
            response += scanner.nextLine() + "\n";
        }

        Matcher matcher = VOTE_ID_PATTERN.matcher(response);
        String id = "";
        if(matcher.find()) {
            id = matcher.group(1);
        }
        System.out.println(response);
        return id;
    }
    
    /**
     * Creates the get request.
     * 
     * @param target    The target file to get.
     * @param address   The address to connect to.
     * @return          The get request.
     */
    public String createGetRequest(String target, InetSocketAddress address) {
        return "GET  " + target +" HTTP/1.0\r\n"
            + "Host: " + address.getHostString() + ":" + address.getPort() + "\r\n"
            + "Accept: */*\r\n"
            + "Connection: Keep-Alive\r\n"
            + "Pragma: no-cache\r\n" 
            + "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; InfoPath.1)\r\n"
            + "\r\n";
    }
}
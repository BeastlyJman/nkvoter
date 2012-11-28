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

package net.sini.nkvoter.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import net.sini.nkvoter.SocketFactory;

/**
 * Created by Sini
 */
public final class TorSocketFactory extends SocketFactory {
    
    /**
     * Constructs a new {@link TorSocketFactory};
     */
    public TorSocketFactory() {}
    
    /**
     *  Default TOR Proxy port.
     */
    private static int PROXY_PORT = 9050;
    /**
     *  Default TOR Proxy hostaddr.
     */
    private static String PROXY_ADDRESS = "localhost";
    
    /**
     * SOCKS4/4a connect request parameter.
     */
    private static final int TOR_CONNECT = 0xF0;
    
    /**
     * The version of SOCKS that the socket utilizes.
     */
    private final static int SOCKS_VERSION = 0x04;

    /**
     * Setting the IP field to 0.0.0.1 causes SOCKS4a to
     * be enabled.
     */
    private final static int SOCKS4A_FAKEIP = 0x01;

    @Override
    public Socket createSocket(InetSocketAddress address) throws IOException {
        Socket s = new Socket(PROXY_ADDRESS, PROXY_PORT);
        DataOutputStream os = new DataOutputStream(s.getOutputStream());
        os.writeByte(SOCKS_VERSION);
        os.writeByte(TOR_CONNECT);
        os.writeShort(address.getPort());
        os.writeInt(SOCKS4A_FAKEIP);
        os.writeByte('\0');
        os.writeBytes(address.getHostString());
        os.writeByte('\0');
        return (s);
    }
}
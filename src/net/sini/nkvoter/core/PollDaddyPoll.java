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

/**
 * Created by Sini
 */
public final class PollDaddyPoll {
    
    /**
     * The poll id.
     */
    private final int pollId;
    
    /**
     * The option id.
     */
    private final int optionId;
    
    /**
     * The poll hash.
     */
    private final String pollHash;
    
    /**
     * The reference url.
     */
    private final String referenceUrl;
    
    /**
     * Constructs a new {@link PollDaddyPoll};
     * 
     * @param pollId        The poll id.
     * @param optionId      The option id.
     * @param pollHash      The poll hash.
     * @param referenceUrl  The reference url.
     */
    public PollDaddyPoll(int pollId, int optionId, String pollHash, String referenceUrl) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.pollHash = pollHash;
        this.referenceUrl = referenceUrl;
    }
    
    /**
     * Gets the poll id.
     * 
     * @return  The poll id.
     */
    public int getPollId() {
        return pollId;
    }
    
    /**
     * Gets the option id.
     * 
     * @return  The option id.
     */
    public int getOptionId() {
        return optionId;
    }

    /**
     * Gets the poll hash.
     * 
     * @return  The poll hash.
     */
    public String getPollHash() {
        return pollHash;
    }
    
    /**
     * Gets the reference url.
     * 
     * @return  The reference url.
     */
    public String getReferenceUrl() {
        return referenceUrl;
    }
}

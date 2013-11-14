/*
 * Copyright (c) 2013, Dwijesh Bhageerutty
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package memory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import memory.node.ConceptNode;

/**
 * Conversation class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:43:20 PM,
 * Nov 7, 2013 Description:
 */
public class Conversation {

    private Date timeStarted;
    private Date timeEnded;
    private boolean live;
    private List<ConceptNode> concepts;
    private LinkedList<String> transcript;

    /**
     * @return the timeStarted
     */
    public Date getTimeStarted() {
        return timeStarted;
    }

    /**
     * @return the timeEnded
     */
    public Date getTimeEnded() {
        return timeEnded;
    }

    /**
     * @return the live
     */
    public boolean isLive() {
        return live;
    }

    /**
     * @return the concepts
     */
    public List<ConceptNode> getConcepts() {
        return concepts;
    }

    /**
     * @return the transcript
     */
    public LinkedList<String> getTranscript() {
        return transcript;
    }
}

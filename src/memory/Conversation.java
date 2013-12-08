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

import java.util.ArrayList;
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

    private long timeStarted;
    private long timeEnded;
    private boolean live;
    private List<ConceptNode> concepts;
    private LinkedList<String> transcript;
    private int no;
    private int noOfNodes;

    public Conversation(int n) {
        this.timeStarted = System.currentTimeMillis();
        this.live = true;
        this.concepts = new ArrayList<>();
        this.transcript = new LinkedList<>();
        this.no = n;
        this.noOfNodes = 0;
    }

    /**
     *
     */
    public void endConversation() {
        timeEnded = System.currentTimeMillis();
        live = false;
    }

    /**
     *
     * @param s
     */
    public void addSentence(String s) {
        transcript.add(s);
    }

    /**
     *
     * @param conceptNode
     */
    public void addConceptNode(ConceptNode conceptNode) {
        concepts.add(conceptNode);
        noOfNodes++;
    }

    /**
     * @return the timeStarted
     */
    public long getTimeStarted() {
        return timeStarted;
    }

    /**
     * @return the timeEnded
     */
    public long getTimeEnded() {
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

    /**
     * @param timeStarted the timeStarted to set
     */
    public void setTimeStarted(long timeStarted) {
        this.timeStarted = timeStarted;
    }

    /**
     * @param timeEnded the timeEnded to set
     */
    public void setTimeEnded(long timeEnded) {
        this.timeEnded = timeEnded;
    }

    /**
     * @param live the live to set
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * @param concepts the concepts to set
     */
    public void setConcepts(List<ConceptNode> concepts) {
        this.concepts = concepts;
    }

    /**
     * @param transcript the transcript to set
     */
    public void setTranscript(LinkedList<String> transcript) {
        this.transcript = transcript;
    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    public int getNoOfNodes() {
        return noOfNodes;
    }
}

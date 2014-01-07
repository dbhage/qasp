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
package session;

import db.IDatabaseConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import memory.Memory;
import memory.node.definition.POS;

/**
 * Session class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 12:55:18 PM,
 * Nov 7, 2013 Description:
 */
public class Session {

    private String sessionID;
    private final Memory memory;
    private boolean memoryInitialized;
    private long startTime;
    private long endTime;
    private boolean live;
    private final IDatabaseConnection dbConn;

    public Session(String sid, IDatabaseConnection dbConn) {
        this.memory = new Memory();
        this.sessionID = sid;
        this.startTime = System.currentTimeMillis();
        this.live = false;
        this.dbConn = dbConn;
    }

    /* getters and setters */
    /**
     * @return the sessionID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * @return the memory
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     * @return the memoryInitialized
     */
    public boolean isMemoryInitialized() {
        return memoryInitialized;
    }

    /**
     * @return the startTime
     */
    public long getStartDate() {
        return startTime;
    }

    /**
     * End the current <code>Conversation</code>
     */
    public void endCurrentConversation() {
        memory.endCurrentConversation();
        live = false;
    }

    /**
     * @param memoryInitialized the memoryInitialized to set
     */
    public void setMemoryInitialized(boolean memoryInitialized) {
        this.memoryInitialized = memoryInitialized;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getNoOfNodesInCurrentConversation() {
        return memory.getNoOfNodesInConversation();
    }

    public int getNoOfNodes() {
        return memory.getNoOfNodes();
    }

    public int getNoOfConversations() {
        return memory.getNoOfConversations();
    }

    /**
     * @return the live
     */
    public boolean isLive() {
        return live;
    }

    /**
     * @param live the live to set
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    public long getCurrentConversationStartTime() {
        return memory.getCurrentConversationStartTime();
    }

    public boolean wordExists(String word) {
        boolean exists = false;
        if (memory.wordExists(word)) {
            return true;
        } else {
            // get from dbase
            String query = "SELECT * FROM definition WHERE trig=\'" + word + "\';";
            ResultSet rs = dbConn.executeQuery(query);
            if (rs == null) {
                return false;
            }
            try {
                while (rs.next()) {
                    exists = true;
                    String trigger = rs.getString("trig");
                    String primeRep = rs.getString("primerep");
                    String pos = rs.getString("pos");
                    memory.addDefinitionNode(trigger, primeRep, POS.stringToPOS(pos));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
        
        if (exists) {
            // just learned a new word
            System.out.println("Learned new word: " + word);
        }
        
        return exists;
    }

    public void saveNewWord(String word, String definition, String pos) {
        // add to memory
        memory.addDefinitionNode(word, definition, POS.stringToPOS(pos));
        // add to database
        String insert = "INSERT INTO definition (trig, primerep, pos, type) VALUES("
                + "\'" + word + "\',"
                + "\'" + definition + "\',"
                + "\'" + pos + "\',"
                + "\'" + "word" + "\');";

        dbConn.executeInsert(insert);
        System.out.println("Learned new word: " + word);
    }

    public void startConversation() {
        memory.startConversation();
        live = true;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}

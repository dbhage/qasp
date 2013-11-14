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

import java.util.ArrayList;
import parser.InputHandler;
import query.QueryHandler;

/**
 * SessionManager class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 12:55:28 PM,
 * Nov 7, 2013 Description:
 */
public class SessionManager {

    private InputHandler inputHandler;
    private QueryHandler queryHandler;
    private ArrayList<Session> sessions;
    private Session currentSession;

    public void createSession() {
    }

    public boolean switchToSession(String sessionId) {
        return true;
    }

    public void handleText(String text) {
    }

    public String handleQuery(String text) {
        return "answer";
    }

    public void saveCurrentSession() {
    }
    
    /**
     * 
     * @param sid
     * @return 
     */
    public boolean saveSessionAs(String sid) {
        // if sid exists return false
        // else save and return true
        return true;
    }

    public boolean currentSessionSaved() {
        return false;
    }
    
    public String getCurrentConversationDuration() {
        return "na";
    }

    public String getConversationStartTime() {
        return "na";
    }
    
    public String getNoOfConversations() {
        return "na";
    }
    
    public String getNoOfNodesInCurrentSession() {
        return "na";
    }
    
    public String getSessionId() {
        return "na";
    }
    
    public String getSessionStartTime() {
        return "na";
    }
    
    public String getNoOfNodesInCurrentConversation() {
        return "na";
    }
    
    public String[] getAvailableSessions() {
        return new String[]{"s0000000001", "efcdavcdsvsdf", "efefewvcesvcew"};
    }
    
    public void endCurrentSession() {
    }
    
    public void endCurrentConversation() {
        
    }
}
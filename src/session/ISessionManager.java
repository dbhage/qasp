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

/**
 * ISessionManager interface.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 9:40:07 PM, Nov 21, 2013 
 * Description: interface for SessionManagers
 */
public interface ISessionManager {
    public void createSession();
    public void switchToSession(String sessionId);
    public void handleText(String text);
    public String handleQuery(String text);
    public void saveCurrentSession();
    public boolean saveSessionAs(String sid);
    public boolean currentSessionSaved();
    public String getCurrentConversationDuration();
    public String getCurrentConversationStartTime();
    public String getNoOfConversations();
    public String getNoOfNodesInCurrentSession();
    public String getNoOfNodesInCurrentConversation();
    public String[] getAvailableSessions();
    public void endCurrentSession();
    public void endCurrentConversation();
    public boolean wordExists(String word);
    public void saveNewWord(String word, String definition, String pos);    
    public void disconnectFromDatabase();
    public String getSessionId();
    public String getSessionStartTime();
}
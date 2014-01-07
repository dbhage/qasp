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

import db.DatabaseConnection;
import db.IDatabaseConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import memory.Memory;
import memory.Prime;
import parser.InputHandler;
import parser.ParserType;
import query.QueryHandler;
import query.cma.CMAType;
import util.DateUtil;

/**
 * SessionManager class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 12:55:28 PM,
 * Nov 7, 2013 Description:
 */
public class SessionManager implements ISessionManager {

    private final InputHandler inputHandler;
    private final QueryHandler queryHandler;
    private Session currentSession;
    private final IDatabaseConnection dbConn;

    public SessionManager() {
        dbConn = new DatabaseConnection("qasp", 3306, "localhost", "dbhage", "qasp");
        dbConn.establishConnection();
        inputHandler = new InputHandler();
        queryHandler = new QueryHandler();

        try {
            loadPrimes();
        } catch (SQLException ex) {
            System.err.println("SQLException when trying to load primes.");
            System.exit(-1);
        }
    }

    @Override
    public void createSession() {
        currentSession = new Session("S" + util.MathUtil.getRandomNumber(100000000, 999999999), dbConn);
    }

    @Override
    public void switchToSession(String sessionId) {
        currentSession = new Session(sessionId, dbConn);
        SessionLoader sessionLoader = new SessionLoader(dbConn, currentSession);
        sessionLoader.load();
    }

    @Override
    public void handleText(String text) {

        if (!currentSession.isLive()) {
            currentSession.startConversation();
        }
        currentSession.getMemory().getCurrentConversation().addSentence(text);
        inputHandler.handleText(text.toLowerCase(), ParserType.SENTENCE, currentSession.getMemory());
    }

    @Override
    public String handleQuery(String text) {
        if (!currentSession.isLive()) {
            currentSession.startConversation();
        }

        currentSession.getMemory().getCurrentConversation().addSentence(text);
        inputHandler.handleText(text.toLowerCase(), ParserType.QUERY, currentSession.getMemory());
        queryHandler.handleQuery(inputHandler.getFrames(), CMAType.ONE, currentSession.getMemory());
        return queryHandler.getAnswer();
    }

    @Override
    public void saveCurrentSession() {
        System.out.println("Saving session.");
        SessionSaver sessionSaver = new SessionSaver(dbConn, currentSession);
        sessionSaver.save();
        System.out.println("Session Saved.");
    }

    @Override
    public boolean saveSessionAs(String sid) {
        currentSession.setSessionID(sid);
        saveCurrentSession();
        return true;
    }

    @Override
    public boolean currentSessionSaved() {
        return false;
    }

    @Override
    public String getCurrentConversationDuration() {
        if (currentSession == null) {
            return "";
        }
        return "1 minute.";
    }

    @Override
    public String getCurrentConversationStartTime() {
        if (currentSession == null) {
            return "";
        }
        return DateUtil.dateToString(currentSession.getCurrentConversationStartTime());
    }

    @Override
    public String getNoOfConversations() {
        if (currentSession == null) {
            return "";
        }
        return currentSession.getNoOfConversations() + "";
    }

    @Override
    public String getNoOfNodesInCurrentSession() {
        if (currentSession == null) {
            return "";
        }
        return currentSession.getNoOfNodes() + "";
    }

    @Override
    public String getSessionId() {
        if (currentSession == null) {
            return "";
        }
        return currentSession.getSessionID();
    }

    /**
     *
     * @return
     */
    @Override
    public String getSessionStartTime() {
        if (currentSession == null) {
            return "";
        }
        return DateUtil.dateToString(currentSession.getStartDate());
    }

    @Override
    public String getNoOfNodesInCurrentConversation() {
        if (currentSession == null) {
            return "";
        }
        return currentSession.getNoOfNodesInCurrentConversation() + "";
    }

    @Override
    public String[] getAvailableSessions() {
        String query = "SELECT sessionid FROM session;";
        ResultSet rs = dbConn.executeQuery(query);
        if (rs == null) {
            return null;
        } else {
            List<String> sessions = new ArrayList<>();
            try {
                while (rs.next()) {
                    sessions.add(rs.getString("sessionid"));
                }
            } catch (SQLException ex) {
                System.err.println("Exception when getting list of sessionids from database");
                ex.printStackTrace();
                System.exit(-1);
            }
            return sessions.toArray(new String[sessions.size()]);
        }
    }

    public void endCurrentSession() {
        if (currentSession != null) {
            currentSession.setEndTime(System.currentTimeMillis());
            currentSession.setLive(false);
        }
    }

    @Override
    public void endCurrentConversation() {
        if (currentSession != null) {
            currentSession.endCurrentConversation();
        }
    }

    @Override
    public boolean wordExists(String word) {
        if (currentSession == null) {
            System.err.println("wordExists being called when no session is active.");
            System.exit(-1);
        }
        return currentSession.wordExists(word);
    }

    @Override
    public void saveNewWord(String word, String definition, String pos) {
        if (currentSession == null) {
            System.err.println("saveNewWord being called when no session is active.");
            System.exit(-1);
        }
        currentSession.saveNewWord(word, definition, pos);
    }

    @Override
    public void disconnectFromDatabase() {
        dbConn.closeConnection();
    }

    private void loadPrimes() throws SQLException {
        ArrayList<Prime> primes = new ArrayList<>();
        String query = "SELECT * FROM primes;";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.err.println("null ResultSet object when executing query: " + query);
            System.exit(-1);
        } else {
            while (rs.next()) {
                String text = rs.getString("text");
                String category = rs.getString("category");
                int valency = Integer.parseInt(rs.getString("valency"));
                primes.add(new Prime(text, category, valency));
            }

            Memory.primes = primes.toArray(new Prime[primes.size()]);
        }
    }
}
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
import memory.Conversation;
import memory.Memory;
import memory.node.ConceptNode;
import memory.node.EventNode;
import memory.node.StateNode;
import memory.node.definition.DefinitionNode;
import memory.node.definition.WordDefinitionNode;

/**
 * SessionSaver class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 9:39:50 PM,
 * Nov 21, 2013 Description: Save a session to the database
 */
public class SessionSaver {

    private final IDatabaseConnection dbConn;
    private final Session session;
    private final Memory memory;
    private final String sessionId;

    public SessionSaver(IDatabaseConnection dbConn, Session session) {
        this.dbConn = dbConn;
        this.session = session;
        this.memory = session.getMemory();
        this.sessionId = session.getSessionID();
    }

    public void save() {
        try {
            saveSessionData();
            if (memory.isEmpty()) {
                System.out.println("Memory empty.");
                return;
            }
            saveEvents();
            saveStates();
            saveWordsOnly();
            saveTextOnly();
            saveConcepts();
            saveConversations();
            saveConversationConceptRelationship();
            saveSessionMoleculesConcept();
        } catch (SQLException ex) {
            System.err.println("Exception while saving memory.");
            System.err.println(ex.toString());
            System.exit(-1);
        }
    }

    private void saveSessionData() throws SQLException {
        String insert = "INSERT INTO session VALUES("
                + "\'" + session.getSessionID() + "\',"
                + session.getStartDate() + ","
                + "0" + ");";
        dbConn.executeInsert(insert);
    }

    private void saveEvents() {
        String insert;
        for (EventNode eventNode : memory.getEventNodes()) {
            insert = "INSERT INTO event VALUES("
                    + eventNode.getId() + ","
                    + "\'" + eventNode.getVerb() + "\',"
                    + "\'" + eventNode.getObject() + "\',"
                    + "\'" + sessionId + "\');";
            dbConn.executeInsert(insert);
        }
    }

    private void saveStates() {
        String insert;
        for (StateNode stateNode : memory.getStateNodes()) {
            insert = "INSERT INTO state VALUES("
                    + stateNode.getId() + ","
                    + "\'" + stateNode.getCharacteristics() + "\',"
                    + "\'" + sessionId + "\');";
            dbConn.executeInsert(insert);
        }
    }

    private void saveWordsOnly() throws SQLException {
        String insert, trigger, query;
        for (WordDefinitionNode word : memory.getWordNodes()) {
            trigger = word.getTrigger();

            query = "SELECT defid FROM definition WHERE trig=\'" + trigger + "\';";
            System.err.println(query);
            ResultSet rs = dbConn.executeQuery(query);

            if (rs == null) {
                System.err.println("defid for word " + trigger + "not found in definition table.");
                System.exit(-1);
            } else {
                if (rs.next()) {
                    int defid = rs.getInt("defid");
                    insert = "INSERT INTO words VALUES("
                            + defid + ","
                            + "\'" + sessionId + "\');";
                    dbConn.executeInsert(insert);
                }
            }
        }
    }

    private void saveTextOnly() throws SQLException {
        String insert, trigger, query;
        for (DefinitionNode text : memory.getTextNodes()) {
            trigger = text.getTrigger();

            query = "SELECT defid FROM definition WHERE trig=\'" + trigger + "\';";
            ResultSet rs = dbConn.executeQuery(query);

            if (rs == null) {
                System.err.println("defid for text " + trigger + "not found in definition table.");
                System.exit(-1);
            } else {
                if (rs.next()) {
                    int defid = rs.getInt("defid");
                    insert = "INSERT INTO text VALUES("
                            + defid + ","
                            + "\'" + sessionId + "\');";
                    dbConn.executeInsert(insert);
                }
            }
        }
    }

    private void saveConcepts() {
        String insert;
        for (ConceptNode conceptNode : memory.getAllConcepts()) {
            insert = "INSERT INTO concept VALUES("
                    + conceptNode.getTypeId() + ","
                    + "\'" + conceptNode.getText() + "\',"
                    + "\'" + conceptNode.getType().toString() + "\',"
                    + "\'" + sessionId + "\');";
            dbConn.executeInsert(insert);
        }
    }

    private void saveConversations() {
        String insert, transcript = "";
        for (Conversation conversation : memory.getConversations()) {
            for (String sentence : conversation.getTranscript()) {
                transcript += sentence + "$$$";
            }

            insert = "INSERT INTO conversation VALUES("
                    + conversation.getNo() + ","
                    + conversation.getTimeStarted() + ","
                    + conversation.getTimeEnded() + ","
                    + "\'" + transcript + "\',"
                    + "\'" + (conversation.isLive() ? 1 : 0) + "\',"
                    + "\'" + sessionId + "\');";
            dbConn.executeInsert(insert);
        }
    }

    private void saveConversationConceptRelationship() {
        String insert;
        for (Conversation conversation : memory.getConversations()) {
            for (ConceptNode conceptNode : conversation.getConcepts()) {
                insert = "INSERT INTO conversationconceptrelationship VALUES("
                        + "\'" + sessionId + "\',"
                        + conversation.getNo() + ","
                        + conceptNode.getTypeId() + ");";
                dbConn.executeInsert(insert);
            }
        }
    }

    private void saveSessionMoleculesConcept() throws SQLException {
        String insert, query;
        for (ConceptNode conceptNode : memory.getAllConcepts()) {
            for (String molecule : conceptNode.getMolecules()) {
                query = "SELECT defid FROM definition WHERE trig=\'" + molecule + "\';";
                ResultSet rs = dbConn.executeQuery(query);

                if (rs == null) {
                    System.err.println("Could not find molecule " + molecule + "'s defid in definition table.");
                    System.exit(-1);
                } else {
                    if (rs.next()) {
                        int defid = rs.getInt("defid");
                        insert = "INSERT INTO sessionmoleculesconcept VALUES("
                                + defid + ","
                                + "\'" + sessionId + "\',"
                                + conceptNode.getTypeId() + ");";
                        dbConn.executeInsert(insert);
                    }
                }
            }
        }
    }
}
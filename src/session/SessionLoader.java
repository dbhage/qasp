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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import memory.Conversation;
import memory.Memory;
import memory.node.ConceptNode;
import memory.node.ConceptType;
import memory.node.EventNode;
import memory.node.StateNode;
import memory.node.definition.DefinitionNode;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;

/**
 * SessionLoader class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 10:41:10 PM, Nov 20, 2013 
 * Description: Load a session from the database.
 */
public class SessionLoader {

    /** database connection */
    private final IDatabaseConnection dbConn;
    
    /** session */
    private final Session session;
    
    /** memory */
    private final Memory memory;
    
    /** session id */
    private final String sessionId;

    /**
     * Constructor for <code>SessionLoader</code>
     *
     * @param dbConnn
     * @param session
     */
    public SessionLoader(IDatabaseConnection dbConnn, Session session) {
        this.session = session;
        this.memory = session.getMemory();

        if (!memory.isEmpty()) {
            System.err.println("Passing MemoryLoader a non-empty memory.");
            System.exit(-1);
        }

        this.dbConn = dbConnn;
        this.sessionId = session.getSessionID();
    }

    /**
     * Load data into <code>Session</code>
     */
    public void load() {
        try {
            loadSessionData();
            loadEvents();
            loadStates();
            loadWordsOnly();
            loadTextOnly();
            loadMoleculesOnly();
            loadConcepts();
            loadConversations();
            loadConversationConceptNodes();
            loadMoleculeConcept();
        } catch (SQLException ex) {
            System.err.println("Exception while loading memory.");
            System.err.print(ex.toString());
            System.exit(-1);
        }
    }

    /**
     * Load Session data
     *
     * @throws SQLException
     */
    private void loadSessionData() throws SQLException {
        String query = "SELECT * FROM session WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.err.println("Session id " + sessionId + " not found in session table!");
            System.exit(-1);
        }

        while (rs.next()) {
            Long timeStarted = rs.getLong("timeStarted");
            session.setStartTime(timeStarted);
        }
    }

    /**
     * load all words
     */
    private void loadWordsOnly() throws SQLException {
        String query = "SELECT * FROM words WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No records in words table.");
            return;
        }

        HashMap<String, List<WordDefinitionNode>> map = new HashMap<>();

        while (rs.next()) {
            String defid = rs.getString("defid");

            query = "SELECT * FROM definition WHERE defid=\'" + defid + "\';";
            ResultSet rsDef = dbConn.executeQuery(query);

            if (rsDef == null) {
                System.err.println("defid obtained from words table is not present in definition table.");
                System.exit(-1);
            }

            String type = rsDef.getString("type");

            if (!type.equals("word")) {
                System.err.println("defid in word table is not a word in definition table. Database corrupt.");
                System.exit(-1);
            }

            String trigger = rsDef.getString("trig");
            String primeRep = rsDef.getString("primerep");
            String pos = rsDef.getString("pos");

            WordDefinitionNode word = new WordDefinitionNode(trigger, primeRep, POS.stringToPOS(pos));

            if (map.get(trigger) == null) {
                ArrayList<WordDefinitionNode> list = new ArrayList<>();
                list.add(word);
                map.put(trigger, list);
            } else {
                map.get(trigger).add(word);
            }
        }
        memory.setWordNodes(map);
    }

    /**
     * Load text only
     */
    private void loadTextOnly() throws SQLException {
        String query = "SELECT * FROM text WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No records in text table.");
            return;
        }

        HashMap<String, List<DefinitionNode>> map = new HashMap<>();

        while (rs.next()) {
            String defid = rs.getString("defid");

            query = "SELECT * FROM definition WHERE defid=\'" + defid + "\';";
            ResultSet rsDef = dbConn.executeQuery(query);

            if (rsDef == null) {
                System.err.println("defid obtained from text table is not present in definition table.");
                System.exit(-1);
            }

            String type = rsDef.getString("type");

            if (!type.equals("text")) {
                System.err.println("defid in text table is not a text in definition table. Database corrupt.");
                System.exit(-1);
            }

            String trigger = rsDef.getString("trig");
            String primeRep = rsDef.getString("primerep");

            DefinitionNode textNode = new DefinitionNode(trigger, primeRep);

            if (map.get(trigger) == null) {
                ArrayList<DefinitionNode> list = new ArrayList<>();
                list.add(textNode);
                map.put(trigger, list);
            } else {
                map.get(trigger).add(textNode);
            }

        }
        memory.setTextNodes(map);
    }

    private void loadMoleculesOnly() throws SQLException {
        String query = "SELECT * FROM definition WHERE type=\'molecule\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No molecule records in definition table.");
            return;
        }

        HashMap<String, DefinitionNode> map = new HashMap<>();

        while (rs.next()) {
            String defid = rs.getString("defid");

            query = "SELECT * FROM definition WHERE defid=\'" + defid + "\';";
            ResultSet rsDef = dbConn.executeQuery(query);

            if (rsDef == null) {
                System.err.println("defid obtained from definition table is not present in definition table.");
                System.exit(-1);
            }

            String type = rsDef.getString("type");

            if (!type.equals("molecule")) {
                System.err.println("Query for molecule returned text/word.");
                System.exit(-1);
            }

            String trigger = rsDef.getString("trig");
            String primeRep = rsDef.getString("primerep");

            DefinitionNode mol = new DefinitionNode(trigger, primeRep);
            map.put(trigger, mol);
        }
        memory.setMoleculeNodes(map);
    }

    /**
     * Load all concepts
     *
     * @throws SQLException
     */
    private void loadConcepts() throws SQLException {
        String query = "SELECT * FROM concepts WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No records in concept table.");
            return;
        }

        HashMap<Integer, ConceptNode> allConcepts = new HashMap<>();

        while (rs.next()) {
            int cid = rs.getInt("conceptid");
            String text = rs.getString("text");
            String type = rs.getString("type");

            ConceptType conceptType = ConceptType.stringToConceptType(type);

            if (conceptType == null) {
                System.err.println("Concept type in database not state nor event. Database corrupt.");
                System.exit(-1);
            }

            // add convo to list
            ConceptNode conceptNode = new ConceptNode(text, conceptType, cid, new ArrayList<String>());
            allConcepts.put(cid, conceptNode);
        }
        memory.setAllConcepts(allConcepts);
    }

    /**
     * Load all conversations
     *
     * @throws SQLException
     */
    private void loadConversations() throws SQLException {
        String query = "SELECT * FROM conversation WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No records in conversation table.");
            return;
        }

        ArrayList<Conversation> convos = new ArrayList<>();

        while (rs.next()) {
            int cid = rs.getInt("convoid");
            long timeStarted = rs.getLong("timeStarted");
            long timeEnded = rs.getLong("timeEnded");
            String transcriptString = rs.getString("transcript");
            int live = rs.getInt("live");
            LinkedList<String> transcriptAsList = new LinkedList<>(Arrays.asList(transcriptString.split("$$$")));

            Conversation convo = new Conversation(cid);
            convo.setTimeStarted(timeStarted);
            convo.setTimeEnded(timeEnded);
            convo.setTranscript(transcriptAsList);
            convo.setLive(live == 1);
            convo.setConcepts(null);

            convos.add(convo);
        }
        memory.setConversations(convos);
    }

    /**
     * load all events
     */
    private void loadEvents() throws SQLException {
        String query = "SELECT * FROM event WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No records in event table.");
            return;
        }

        HashMap<Integer, EventNode> map = new HashMap<>();

        while (rs.next()) {
            int id = rs.getInt("eventid");
            String verb = rs.getString("verb");
            String object = rs.getString("object");

            EventNode node = new EventNode(id, verb, object);
            map.put(id, node);
        }
        memory.setEventNodes(map);
    }

    /**
     * load all states
     */
    private void loadStates() throws SQLException {
        String query = "SELECT * FROM state WHERE sessionid=\'" + sessionId + "\';";
        ResultSet rs = dbConn.executeQuery(query);

        if (rs == null) {
            System.out.println("No records in event table.");
            return;
        }

        HashMap<Integer, StateNode> map = new HashMap<>();

        while (rs.next()) {
            int id = rs.getInt("stateid");
            String characteristics = rs.getString("characteristics");

            StateNode node = new StateNode(id, characteristics);
            map.put(id, node);
        }
        memory.setStateNodes(map);
    }

    /**
     * Conversations and Concepts have already been loaded in. DO NOT CREATE NEW
     * OBJECTS HERE! Find the ids and find and link the relevant existing
     * objects.
     */
    private void loadConversationConceptNodes() throws SQLException {
        for (Conversation conversation : memory.getConversations()) {
            String query = "SELECT conceptid FROM conversationconceptrelationship WHERE sessionid=\'" + sessionId
                    + "\', convoid=\'" + conversation.getNo() + "\';";
            ResultSet rs = dbConn.executeQuery(query);

            if (rs == null) {
                continue;
            }

            while (rs.next()) {
                int conceptId = rs.getInt("conceptid");
                ConceptNode conceptNode = memory.getConcept(conceptId);
                if (conceptNode == null) {
                    // error
                    System.err.println("Concept with id " + conceptId + " found in database but not yet loaded in memory.");
                    System.exit(-1);
                } else {
                    conversation.addConceptNode(conceptNode);
                }
            }
        }
    }

    private void loadMoleculeConcept() throws SQLException {
        for (ConceptNode conceptNode : memory.getAllConcepts()) {
            String query = "SELECT defid FROM sessionmoleculesconcept WHERE sessionid=\'" + sessionId
                    + "\', conceptid=\'" + conceptNode.getTypeId() + "\';";
            ResultSet rs = dbConn.executeQuery(query);

            if (rs == null) {
                continue;
            }

            while (rs.next()) {
                int defid = rs.getInt("defid");

                query = "SELECT trig FROM definition WHERE defid=\'" + defid + "\';";
                ResultSet rsDef = dbConn.executeQuery(query);

                String trigger = rsDef.getString("trig");
                conceptNode.addMolecule(trigger);
            }
        }
    }
}
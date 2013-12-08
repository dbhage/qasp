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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import memory.node.ConceptNode;
import memory.node.ConceptType;
import memory.node.EventNode;
import memory.node.StateNode;
import memory.node.definition.DefinitionNode;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;

/**
 * Memory class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:09:27 PM,
 * Nov 7, 2013 Description:
 */
public class Memory {

    /**
     * list of event nodes
     */
    private HashMap<Integer, EventNode> eventNodes;

    /**
     * list of state nodes
     */
    private HashMap<Integer, StateNode> stateNodes;

    /**
     * list of molecules
     */
    private HashMap<String, DefinitionNode> moleculeNodes;

    /**
     * list of all definition nodes
     */
    private HashMap<String, List<DefinitionNode>> defNodes;

    /**
     * list of text definition nodes
     */
    private HashMap<String, List<DefinitionNode>> textNodes;

    /**
     * list of word definition nodes
     */
    private HashMap<String, List<WordDefinitionNode>> wordNodes;

    /**
     * list of verb nodes
     */
    private HashMap<String, List<WordDefinitionNode>> verbNodes;

    /**
     * list of all concepts
     */
    private HashMap<Integer, ConceptNode> allConcepts;

    /**
     * array of semantic primes
     */
    public static Prime[] primes;

    /**
     * list of conversations
     */
    private ArrayList<Conversation> conversations;

    /**
     * map of molecules to a list of concept nodes
     */
    private HashMap<String, List<ConceptNode>> moleculesToConceptMap;

    /**
     * This will be used to link concepts, events and states.
     */
    private int conceptNo;

    /**
     * the current <code>Conversation</code>
     */
    private Conversation currentConversation;

    public Memory() {
        this.eventNodes = new HashMap();
        this.stateNodes = new HashMap();
        this.moleculeNodes = new HashMap();
        this.defNodes = new HashMap();
        this.textNodes = new HashMap();
        this.wordNodes = new HashMap();
        this.verbNodes = new HashMap();
        this.allConcepts = new HashMap();
        this.conversations = new ArrayList();
        this.moleculesToConceptMap = new HashMap();
        this.conceptNo = 0;
    }

    /**
     * Get the <code>ConceptNode</code>s related to the molecule
     *
     * @param mol - the molecule
     * @return <code>List<ConceptNode></code>
     */
    public List<ConceptNode> getConceptNodesForMolecule(String mol) {
        return moleculesToConceptMap.get(mol);
    }

    /**
     * Add a <code>DefinitionNode</code> to memory
     *
     * @param trigger
     * @param primeRep
     * @param pos
     * @return <code>true</code> if node successfully added, <code>false</code>
     * otherwise.
     */
    public boolean addDefinitionNode(String trigger, String primeRep, POS pos) throws NullPointerException {
        if (primeRep == null) {
            throw new NullPointerException("Prime rep for " + trigger + " is null.");
        }
        DefinitionNode node;
        if (trigger.split(" ").length == 1) {
            // its a word
            node = new WordDefinitionNode(trigger, primeRep, pos);

            // if a list does not already exist, create one with a WordDefNode and add it 
            // to the map else, just add to the list corresponding to the trigger
            if (wordNodes.get(trigger) == null) {
                ArrayList<WordDefinitionNode> list = new ArrayList<>();
                list.add((WordDefinitionNode) node);
                wordNodes.put(trigger, list);
            } else {
                wordNodes.get(trigger).add((WordDefinitionNode) node);
            }

            if (pos.equals(POS.V)) {
                if (verbNodes.get(trigger) == null) {
                    ArrayList<WordDefinitionNode> list = new ArrayList<>();
                    list.add((WordDefinitionNode) node);
                    verbNodes.put(trigger, list);
                } else {
                    verbNodes.get(trigger).add((WordDefinitionNode) node);
                }
            }
        } else {
            node = new DefinitionNode(trigger, primeRep);
            if (trigger.startsWith("#")) {
                // its a molecule
                moleculeNodes.put(trigger, node);
            } else {
                // its a text
                if (textNodes.get(trigger) == null) {
                    ArrayList<DefinitionNode> list = new ArrayList<>();
                    list.add((DefinitionNode) node);
                    textNodes.put(trigger, list);
                } else {
                    textNodes.get(trigger).add(node);
                }
            }
        }

        if (defNodes.get(trigger) == null) {
            ArrayList<DefinitionNode> list = new ArrayList<>();
            list.add((DefinitionNode) node);
            defNodes.put(trigger, list);
        } else {
            defNodes.get(trigger).add(node);
        }
        return true;
    }

    public void addConceptNode(String text, ConceptType type, ArrayList<String> molecules) {
        conceptNo++;
        ConceptNode conceptNode = new ConceptNode(text, type, conceptNo, molecules);
        System.out.println("Adding Concept Node #" + conceptNo);
        allConcepts.put(conceptNo, conceptNode);
        currentConversation.addConceptNode(conceptNode);
    }

    public void addStateNode(String c) {
        System.out.println("Adding State Node #" + conceptNo);
        stateNodes.put(conceptNo, new StateNode(conceptNo, c));
    }

    public void addEventNode(String v, String o) {
        System.out.println("Adding Event Node #" + conceptNo);
        eventNodes.put(conceptNo, new EventNode(conceptNo, v, o));
    }

    public List<DefinitionNode> getDefinitionNodes(String trigger) {
        return defNodes.get(trigger);
    }

    public long getCurrentConversationStartTime() {
        if (currentConversation == null) {
            return 0;
        }
        return currentConversation.getTimeStarted();
    }

    public int getNoOfConversations() {
        return conversations.size();
    }

    public int getNoOfNodes() {
        return defNodes.size() + allConcepts.size() + stateNodes.size() + eventNodes.size();
    }

    public void startConversation() {
        System.out.println("Starting Conversation #" + (conversations.size() + 1));
        currentConversation = new Conversation(conversations.size() + 1);
        conversations.add(currentConversation);
    }

    public void endCurrentConversation() {
        if (currentConversation != null) {
            System.out.println("Ending conversation #" + currentConversation.getNo());
            currentConversation.endConversation();
        } else {
            System.out.println("currentConversation null.");
        }
    }

    public ConceptNode getConcept(int id) {
        return allConcepts.get(id);
    }

    public DefinitionNode getMoleculeNode(String trig) {
        return moleculeNodes.get(trig);
    }

    public boolean isEmpty() {
        if (eventNodes.isEmpty() && stateNodes.isEmpty() && moleculeNodes.isEmpty()
                && defNodes.isEmpty() && textNodes.isEmpty() && wordNodes.isEmpty()
                && verbNodes.isEmpty() && allConcepts.isEmpty() && conversations.isEmpty()
                && moleculesToConceptMap.isEmpty()) {
            if (conceptNo != 1) {
                System.err.println("Memory empty but conceptNo is not equal to 1.");
                System.exit(-1);
            }
            return true;
        }
        return false;
    }

    public int getNoOfNodesInConversation() {
        if (currentConversation == null) {
            return 0;
        }
        return currentConversation.getNoOfNodes();
    }

    public boolean wordExists(String word) {
        return (wordNodes.get(word) != null);
    }

    /* getters and setters for fields */
    /**
     * @return the eventNodes
     */
    public Collection<EventNode> getEventNodes() {
        return eventNodes.values();
    }

    /**
     * @param eventNodes the eventNodes to set
     */
    public void setEventNodes(HashMap<Integer, EventNode> eventNodes) {
        this.eventNodes = eventNodes;
    }

    /**
     * @return the stateNodes
     */
    public Collection<StateNode> getStateNodes() {
        return stateNodes.values();
    }

    /**
     * @param stateNodes the stateNodes to set
     */
    public void setStateNodes(HashMap<Integer, StateNode> stateNodes) {
        this.stateNodes = stateNodes;
    }

    /**
     * @return the moleculeNodes
     */
    public Collection<DefinitionNode> getMoleculeNodes() {
        return moleculeNodes.values();
    }

    /**
     * @param moleculeNodes the moleculeNodes to set
     */
    public void setMoleculeNodes(HashMap<String, DefinitionNode> moleculeNodes) {
        this.moleculeNodes = moleculeNodes;
    }

    /**
     * @return the defNodes
     */
    public Collection<DefinitionNode> getDefNodes() {
        Collection<DefinitionNode> coll = new ArrayList<>();
        for (List<DefinitionNode> nodeLists : defNodes.values()) {
            coll.addAll(coll);
        }
        return coll;
    }

    /**
     * @param defNodes the defNodes to set
     */
    public void setDefNodes(HashMap<String, List<DefinitionNode>> defNodes) {
        this.defNodes = defNodes;
    }

    /**
     * @return the textNodes
     */
    public Collection<DefinitionNode> getTextNodes() {
        Collection<DefinitionNode> coll = new ArrayList<>();
        for (List<DefinitionNode> nodeLists : textNodes.values()) {
            coll.addAll(coll);
        }
        return coll;
    }

    /**
     * @return the allConcepts
     */
    public Collection<ConceptNode> getAllConcepts() {
        return allConcepts.values();
    }

    /**
     * @param allConcepts the allConcepts to set
     */
    public void setAllConcepts(HashMap<Integer, ConceptNode> allConcepts) {
        this.allConcepts = allConcepts;
    }

    /**
     * @return the conversations
     */
    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    /**
     * @param conversations the conversations to set
     */
    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    /**
     * @return the moleculesToConceptMap
     */
    public HashMap<String, List<ConceptNode>> getMoleculesToConceptMap() {
        return moleculesToConceptMap;
    }

    /**
     * @param moleculesToConceptMap the moleculesToConceptMap to set
     */
    public void setMoleculesToConceptMap(HashMap<String, List<ConceptNode>> moleculesToConceptMap) {
        this.moleculesToConceptMap = moleculesToConceptMap;
    }

    /**
     * @return the conceptNo
     */
    public int getConceptNo() {
        return conceptNo;
    }

    /**
     * @return the currentConversation
     */
    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    public EventNode getEvent(int typeId) {
        return eventNodes.get(typeId);
    }

    /**
     * @return the wordNodes
     */
    public Collection<WordDefinitionNode> getWordNodes() {
        Collection<WordDefinitionNode> coll = new ArrayList<>();
        for (List<WordDefinitionNode> list : wordNodes.values()) {
            coll.addAll(list);
        }
        return coll;
    }

    public List<WordDefinitionNode> getWordNodes(String trigger) {
        return wordNodes.get(trigger);
    }
    
    /**
     * @param wordNodes the wordNodes to set
     */
    public void setWordNodes(HashMap<String, List<WordDefinitionNode>> wordNodes) {
        this.wordNodes = wordNodes;
    }

    /**
     * @return the verbNodes
     */
    public HashMap<String, List<WordDefinitionNode>> getVerbNodes() {
        return verbNodes;
    }

    /**
     * @param verbNodes the verbNodes to set
     */
    public void setVerbNodes(HashMap<String, List<WordDefinitionNode>> verbNodes) {
        this.verbNodes = verbNodes;
    }

    /**
     * @param textNodes the textNodes to set
     */
    public void setTextNodes(HashMap<String, List<DefinitionNode>> textNodes) {
        this.textNodes = textNodes;
    }

    public boolean hasDefinitionNode(String subject, String primeDef) {
        List<DefinitionNode> nodes = defNodes.get(subject);
        if (nodes == null || nodes.isEmpty()) {
            return false;
        }
        
        for (DefinitionNode def : nodes) {
            if (def.getPrimeRepresentation().equals(primeDef)) return true;
        }
        
        return false;
    }
    
    public DefinitionNode getDefinitionNode(String s) {
        return defNodes.get(s).get(0);
    }
}

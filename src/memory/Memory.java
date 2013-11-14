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

    private ArrayList<EventNode> eventNodes;

    private ArrayList<StateNode> stateNodes;

    /* Definition Nodes */
    private ArrayList<DefinitionNode> moleculeNodes;

    private ArrayList<DefinitionNode> defNodes;

    private ArrayList<DefinitionNode> textNodes;

    private ArrayList<WordDefinitionNode> wordNodes;

    private ArrayList<WordDefinitionNode> verbNodes;

    /**
     * *****************
     */
    private ArrayList<ConceptNode> allConcepts;

    private Prime[] primes;

    private ArrayList<Conversation> conversations;

    private HashMap<String, List<ConceptNode>> moleculesToConceptMap;

    public Memory() {

    }

    public List<ConceptNode> getConceptNodesForMolecule(String mol) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public boolean addDefinitionNode(String trigger, String primeRep, POS pos) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public boolean addConceptNode(String text, ConceptType type) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public boolean addStateNode(int n, String c) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public boolean addEventNode(int n, String v, String o) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public DefinitionNode getDefinitionNode(String trigger) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public Date getCurrentConversationStartTime() {
        // TODO check for runtime exceptions
        return conversations.get(conversations.size() - 1).getTimeStarted();
    }

    public int getNoOfConversations() {
        return conversations.size();
    }

    public int getNoOfNodes() {
        // TODO
        return 0;
    }
}

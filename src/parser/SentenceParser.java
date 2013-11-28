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
package parser;

import frame.AFrame;
import frame.SVOFrame;
import java.util.ArrayList;
import java.util.List;
import memory.Memory;
import memory.node.ConceptType;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;

/**
 * SentenceParser class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:01:18 PM,
 Nov 7, 2013 Description:
 */
public class SentenceParser implements IParser {

    public final Memory memory;

    public SentenceParser(Memory memory) {
        this.memory = memory;
    }

    @Override
    public void parse(String text) {

        WordDefinitionNode[] tokenizedSentence = tokenizeSentence(text);

        AFrame frame;

        frame = processSimpleSentence(text, tokenizedSentence);

        populateMemory((SVOFrame) frame);

    }

    private WordDefinitionNode[] tokenizeSentence(String sentence) {
        List<WordDefinitionNode> tokens = new ArrayList<>();
        String[] splitSentence = sentence.split(" ");
        for (String s : splitSentence) {
            tokens.add((WordDefinitionNode) memory.getDefinitionNode(s));
        }
        return tokens.toArray(new WordDefinitionNode[tokens.size()]);
    }

    /**
     * Simple SVO
     *
     * @param sentence
     * @return
     */
    private AFrame processSimpleSentence(String text, WordDefinitionNode[] sentence) {
        SVOFrame svoFrame = new SVOFrame();
        for (WordDefinitionNode word : sentence) {
            if (word.getPos().equals(POS.V)) {
                svoFrame.setVerb(word.getTrigger());
            }
        }
        svoFrame.setSubject(text.split(svoFrame.getVerb())[0]);
        svoFrame.setObject(text.split(svoFrame.getVerb())[1]);
        return svoFrame;
    }

    /**
     * Create nodes and store them in memory
     *
     * @param frame
     */
    private void populateMemory(SVOFrame frame) {

        if (isState(frame.getVerb())) {
            memory.addConceptNode(frame.getSubject(), ConceptType.STATE);
            memory.addStateNode(frame.getObject());
        } else {
            memory.addConceptNode(frame.getSubject(), ConceptType.EVENT);
            memory.addEventNode(frame.getVerb(), frame.getObject());
        }

        if (memory.getDefinitionNode(frame.getSubject()) == null)
            memory.addDefinitionNode(frame.getSubject(), generatePrimeRepForText(frame.getSubject()), POS.S);

        if (memory.getDefinitionNode(frame.getObject()) == null)
            memory.addDefinitionNode(frame.getObject(), generatePrimeRepForText(frame.getObject()), POS.O);
    }

    private boolean isState(String verb) {
        return (verb.equals("are") || verb.equals("is") || verb.equals("was") || verb.equals("was"));
    }

    private String generatePrimeRepForText(String text) {
        String primeRep = "";

        boolean the = false, in = false;
        String [] splitText = text.split(" ");
        for (String s : splitText) {
            if (s.equals("")) continue;
            if (s.equals("the")) {
                the = true;
                primeRep += "the(";
                continue;
            } else if (s.equals("in")) {
                in = true;
                primeRep += "inside(";
                continue;
            }
            if (the) {
                if (((WordDefinitionNode)(memory.getDefinitionNode(s))).getPos().equals(POS.N) 
                        || (splitText[splitText.length-1]==s)) {
                    primeRep += memory.getDefinitionNode(s).getPrimeRepresentation() + ")$$";
                    the = false;
                } else {
                    primeRep += memory.getDefinitionNode(s).getPrimeRepresentation() + ",";
                }
            } else if (in) {
                if (((WordDefinitionNode)(memory.getDefinitionNode(s))).getPos().equals(POS.N)
                        || (splitText[splitText.length-1]==s)) {
                    primeRep += memory.getDefinitionNode(s).getPrimeRepresentation() + ")$$";
                    in = false;
                } else {
                    primeRep += memory.getDefinitionNode(s).getPrimeRepresentation() + ",";
                }
            } else {
                primeRep += memory.getDefinitionNode(s).getPrimeRepresentation() + "$$";
            }
        }
        System.out.println(primeRep);
        return primeRep;
    }
}
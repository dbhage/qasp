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
import java.util.Arrays;
import java.util.List;
import memory.Memory;
import memory.node.ConceptType;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;

/**
 * SentenceParser class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:01:18 PM,
 * Nov 7, 2013 Description:
 */
public class SentenceParser extends AParser implements IParser {

    public SentenceParser(Memory memory) {
        super(memory);
    }

    @Override
    public void parse(String text) {
        WordDefinitionNode[][] tokenizedSentences = tokenizeSentence(text);
        AFrame[] frames;
        for (WordDefinitionNode[] tokenizedSentence : tokenizedSentences) {
            if (!(text.contains(" and ") || text.contains(" or "))) {
                // simple sentence
                AFrame frame = processSimpleSentence(text, tokenizedSentence, false);
                populateMemory(((SVOFrame) frame).getSubject(), (SVOFrame) frame, tokenizedSentence);
            } else if (text.contains(" and ") && !text.contains(" or ")) {
                try {
                    processAndCase1(text, tokenizedSentence);
                } catch (IllegalArgumentException e) {
                    processAndCase2(text, tokenizedSentence);
                }
            } else {
                throw new UnsupportedOperationException("not supported yet");
            }
        }
    }

    /**
     * Expected Sentence Format: S V O
     *
     * @param text
     * @param sentence
     * @return
     */
    private AFrame processSimpleSentence(String text, WordDefinitionNode[] sentence, boolean t) 
            throws IllegalArgumentException {
        String tab;
        if (t) {
            tab = "  ";
        } else {
            tab = "";
        }

        System.out.println(tab + "Attempting to apply processSimpleSentence.");

        SVOFrame svoFrame = new SVOFrame();
        for (WordDefinitionNode word : sentence) {
            if (word.getPos().equals(POS.V)) {
                svoFrame.setVerb(word.getTrigger());
            }
        }
        svoFrame.setSubject(text.split(svoFrame.getVerb())[0]);
        String o = text.split(svoFrame.getVerb())[1];
        if (o.startsWith(" ")) {
            svoFrame.setObject(o.substring(1, o.length()));
        } else {
            svoFrame.setObject(o);
        }

        if (svoFrame.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject is empty");
        }
        
        System.out.println(tab + "Generated: " + svoFrame.toString());
        System.out.println(tab + "Applied processSimpleSentence Successfully.");
        return svoFrame;
    }

    /**
     * Expected Sentence Format: S1 V1 O1 and S2 V2 O2
     *
     * @param text
     * @param sentence
     * @return
     */
    private void processAndCase1(String text, WordDefinitionNode[] sentence) throws IllegalArgumentException {
        try {
            System.out.println("Attempting to apply processAndCase1.");

            String[] parts = text.split(" and "); // split in 2

            WordDefinitionNode[] part0, part1;

            // get the definiton nodes for each part
            List<WordDefinitionNode> sentenceAsList = Arrays.asList(sentence);
            part0 = sentenceAsList.subList(0, parts[0].split(" ").length)
                    .toArray(new WordDefinitionNode[parts[0].split(" ").length]);
            part1 = sentenceAsList.subList(parts[0].split(" ").length + 1, sentence.length)
                    .toArray(new WordDefinitionNode[parts[1].split(" ").length]);

            // at this point, we can just call process simple sentence        
            AFrame[] frames = new AFrame[2];

            frames[0] = processSimpleSentence(parts[0], part0, true);
            frames[1] = processSimpleSentence(parts[1], part1, true);
            
            populateMemory(((SVOFrame) frames[0]).getSubject(), (SVOFrame) frames[0], part0);
            populateMemory(((SVOFrame) frames[1]).getSubject(), (SVOFrame) frames[1], part1);

            System.out.println("Applied processANDcase1 successfully.");
        } catch (Exception e) {
            System.out.println("Failed processANDcase1");
            throw new IllegalArgumentException("Failed processANDcase1");
        }
    }

    /**
     * S V1 O1 and V2 O2
     *
     * @param text
     * @param sentence
     * @return
     */
    private void processAndCase2(String text, WordDefinitionNode[] sentence) {
        System.out.println("Attempting to apply processAndCase2.");

        String[] parts = text.split(" and "); // split in 2
        WordDefinitionNode[] part0, part1;

        List<WordDefinitionNode> sentenceAsList = Arrays.asList(sentence);

        // first part S V1 O1
        part0 = sentenceAsList.subList(0, parts[0].split(" ").length)
                .toArray(new WordDefinitionNode[parts[0].split(" ").length]);

        AFrame[] frames = new AFrame[2];
        frames[0] = processSimpleSentence(parts[0], part0, true);

        // second part S V2 O2
        List<WordDefinitionNode> part1List = new ArrayList<>();
        List<WordDefinitionNode> s = sentenceAsList.subList(0, ((SVOFrame) frames[0]).getSubject().split(" ").length);
        List<WordDefinitionNode> v2o2 = sentenceAsList.subList(parts[0].split(" ").length + 1, sentenceAsList.size());
        part1List.addAll(s);
        part1List.addAll(v2o2);

        part1 = part1List.toArray(new WordDefinitionNode[part1List.size()]);

        frames[1] = processSimpleSentence(((SVOFrame) frames[0]).getSubject() + " " + parts[1], part1, true);

        populateMemory(((SVOFrame) frames[0]).getSubject(), (SVOFrame) frames[0], part0);
        populateMemory(((SVOFrame) frames[1]).getSubject(), (SVOFrame) frames[1], part1);

        System.out.println("Applied processANDcase2 successfully.");
    }

    private AFrame[] processAndCase3(String text, WordDefinitionNode[] sentence) {
        throw new UnsupportedOperationException("and case 3");
    }

    private AFrame[] processAndCase4(String text, WordDefinitionNode[] sentence) {
        throw new UnsupportedOperationException("and case 4");
    }

    private AFrame[] processAndCase5(String text, WordDefinitionNode[] sentence) {
        throw new UnsupportedOperationException("and case 5");
    }

    private void populateMemory(String text, SVOFrame frame, WordDefinitionNode[] nodes) {
        ArrayList<String> molecules = new ArrayList<>();

        List<WordDefinitionNode> subjectNodes = new ArrayList<>();
        List<WordDefinitionNode> objectNodes = new ArrayList<>();

        int subIndex = frame.getSubject().split(" ").length;

        for (int i = 0; i < nodes.length; i++) {
            String[] wordMolecules = nodes[i].getMolecules();

            if (wordMolecules.length != 0) {
                molecules.addAll(Arrays.asList(wordMolecules));
            }

            if (i < subIndex) {
                subjectNodes.add(nodes[i]);
            }

            if (i > subIndex) {
                objectNodes.add(nodes[i]);
            }
        }

        if (isState(frame.getVerb())) {
            memory.addConceptNode(frame.getSubject(), ConceptType.STATE, molecules);
            memory.addStateNode(frame.getObject());
        } else {
            memory.addConceptNode(frame.getSubject(), ConceptType.EVENT, molecules);
            memory.addEventNode(frame.getVerb(), frame.getObject());
        }

        // definition nodes for subject
        String primeDefSubject = generatePrimeRepForText(frame.getSubject().split(" "), subjectNodes.toArray(new WordDefinitionNode[subjectNodes.size()]));

        if (memory.hasDefinitionNode(frame.getSubject(), primeDefSubject)) {
            memory.addDefinitionNode(frame.getSubject(), primeDefSubject, POS.S);
        }

        // definition nodes for object
        String primeDefObject = generatePrimeRepForText(frame.getObject().split(" "), objectNodes.toArray(new WordDefinitionNode[objectNodes.size()]));

        if (memory.hasDefinitionNode(frame.getObject(), primeDefObject)) {
            memory.addDefinitionNode(frame.getObject(), primeDefObject, POS.O);
        }
    }
}

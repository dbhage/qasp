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
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 1:01:18 PM, Nov 7, 2013 
 * Description:
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
                try {
                    AFrame frame = processSimpleSentence(text, tokenizedSentence, false);
                    populateMemory((SVOFrame) frame, tokenizedSentence);
                    System.out.println("Applied processSimpleSentence.");
                } catch (IllegalArgumentException ex) {
                    System.out.println("Sentence cannot be parsed.");
                }
            } else if (text.contains(" and ") && !text.contains(" or ")) {
                try {
                    processAndCase1(text, tokenizedSentence);
                    System.out.println("Applied processAndCase1.");
                } catch (IllegalArgumentException e) {
                    try {
                        processAndCase2(text, tokenizedSentence);
                        System.out.println("Applied processAndCase2.");

                    } catch (IllegalArgumentException e1) {
                        try {
                            processAndCase3(text, tokenizedSentence);
                            System.out.println("Applied processAndCase3.");

                        } catch (IllegalArgumentException e2) {
                            try {
                                processAndCase4(text, tokenizedSentence);
                                System.out.println("Applied processAndCase4.");

                            } catch (IllegalArgumentException e3) {
                                try {
                                    processAndCase5(text, tokenizedSentence);
                                    System.out.println("Applied processAndCase5.");

                                } catch (IllegalArgumentException e4) {
                                    System.out.println("Sentence cannot be parsed.");
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("Sentence cannot be parsed.");
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
        try {
            String tab;
            if (t) {
                tab = "  ";
            } else {
                tab = "";
            }

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

            if (svoFrame.getSubject().isEmpty() || svoFrame.getVerb().isEmpty()) {
                throw new IllegalArgumentException("Subject is empty");
            }

            return svoFrame;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("ProcessSimpleSentence failed.");
        }
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

            populateMemory((SVOFrame) frames[0], part0);
            populateMemory((SVOFrame) frames[1], part1);

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Failed processANDcase1");
        }
    }

    /**
     * Expected Sentence Format: S V1 O1 and V2 O2
     *
     * @param text
     * @param sentence
     * @return
     */
    private void processAndCase2(String text, WordDefinitionNode[] sentence) throws IllegalArgumentException {
        try {
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

            populateMemory((SVOFrame) frames[0], part0);
            populateMemory((SVOFrame) frames[1], part1);

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Failed to apply processANDcase2.");
        }
    }

    /**
     * Expected Sentence Format: S V O1 and O2
     *
     * @param text
     * @param sentence
     * @return
     */
    private void processAndCase3(String text, WordDefinitionNode[] sentence) {
        try {
            String[] parts = text.split(" and "); // split in 2
            WordDefinitionNode[] part0, part1;

            List<WordDefinitionNode> sentenceAsList = Arrays.asList(sentence);

            // first part S V1 O1
            part0 = sentenceAsList.subList(0, parts[0].split(" ").length)
                    .toArray(new WordDefinitionNode[parts[0].split(" ").length]);

            SVOFrame[] frames = new SVOFrame[2];
            frames[0] = (SVOFrame) processSimpleSentence(parts[0], part0, true);

            // second part
            frames[1] = frames[0].clone();
            frames[1].setObject(parts[1]);

            int index = 0;
            for (int i = 0; i < sentenceAsList.size(); i++) {
                if (sentenceAsList.get(i).isVerb()) {
                    index = i;
                    break;
                }
            }

            List<WordDefinitionNode> part1List = new ArrayList<>();
            List<WordDefinitionNode> sv = sentenceAsList.subList(0, index + 1);
            List<WordDefinitionNode> o2 = sentenceAsList.subList(
                    index + frames[0].getObject().split(" ").length + 1, sentenceAsList.size());

            part1List.addAll(sv);
            part1List.addAll(o2);

            part1 = part1List.toArray(new WordDefinitionNode[part1List.size()]);

            populateMemory(frames[0], part0);
            populateMemory(frames[1], part1);

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Failed to apply processANDcase2.");
        }
    }

    /**
     * Expected Format: S1 and S2 V O
     *
     * @param text
     * @param sentence
     * @return
     * @throws IllegalArgumentException
     */
    private void processAndCase4(String text, WordDefinitionNode[] sentence) throws IllegalArgumentException {
        try {
            String[] parts = text.split(" and "); // split in 2
            WordDefinitionNode[] part0, part1;

            List<WordDefinitionNode> sentenceAsList = Arrays.asList(sentence);

            // second part S2 V O
            part1 = sentenceAsList.subList(parts[0].split(" ").length + 1, sentence.length)
                    .toArray(new WordDefinitionNode[parts[1].split(" ").length]);

            SVOFrame[] frames = new SVOFrame[2];
            frames[1] = (SVOFrame) processSimpleSentence(parts[1], part1, true);

            // first part S1 V O
            frames[0] = frames[1].clone();
            frames[1].setSubject(parts[0]);
            List<WordDefinitionNode> s1 = sentenceAsList.subList(0, parts[0].split(" ").length);

            int index = 0;
            for (int i = 0; i < sentenceAsList.size(); i++) {
                if (sentenceAsList.get(i).isVerb()) {
                    index = i;
                    break;
                }
            }
            List<WordDefinitionNode> vo = sentenceAsList.subList(index, sentenceAsList.size());

            List<WordDefinitionNode> part0List = new ArrayList<>();
            part0List.addAll(s1);
            part0List.addAll(vo);

            part0 = part0List.toArray(new WordDefinitionNode[part0List.size()]);

            // populate
            populateMemory(frames[0], part0);
            populateMemory(frames[1], part1);

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Failed to apply processANDcase2.");
        }
    }

    /**
     * Expected Format: S V1 and V2 O Need to deal with verb valency here. Can
     * end up with frame(s,v1,o) and frame(s,v2,o) OR frame(s,v1,null) and
     * frame(s,v2,o)
     *
     * @param text
     * @param sentence
     * @throws IllegalArgumentException
     */
    private void processAndCase5(String text, WordDefinitionNode[] sentence) throws IllegalArgumentException {
        try {
            String[] parts = text.split(" and "); // split in 2
            WordDefinitionNode[] part0, part1;

            List<WordDefinitionNode> sentenceAsList = Arrays.asList(sentence);

            // second part S V2 O
            int verb1Index = 0;
            for (int i = 0; i < sentenceAsList.size(); i++) {
                if (sentenceAsList.get(i).isVerb()) {
                    verb1Index = i;
                    break;
                }
            }

            String v1 = sentenceAsList.get(verb1Index).getTrigger();

            List<WordDefinitionNode> part1List = new ArrayList<>();
            List<WordDefinitionNode> s = sentenceAsList.subList(0, verb1Index);
            List<WordDefinitionNode> v2o = sentenceAsList.subList(parts[0].split(" ").length + 1, sentenceAsList.size());
            part1List.addAll(s);
            part1List.addAll(v2o);
            part1 = part1List.toArray(new WordDefinitionNode[part1List.size()]);

            SVOFrame[] frames = new SVOFrame[2];
            String textForFrame1 = parts[0].split(v1)[0] + parts[1];
            frames[1] = (SVOFrame) processSimpleSentence(textForFrame1, part1, true);

            // now check first part
            frames[0] = frames[1].clone();
            frames[0].setVerb(v1);

            // check if we need to keep object or not.
            boolean needToKeepObject = false;
            List<WordDefinitionNode> part0List = new ArrayList<>();

            // assign needToKeepObject a value based on valency
            // need to check if verb1 and object have anything in common
            String[] verb1Molecules = sentenceAsList.get(verb1Index).getMolecules();
            String[] objectMolecules;
            List<String> objectMoleculesAsList = new ArrayList<>();

            for (int i = 0; i < v2o.size(); i++) {
                if (i == 0) {
                    continue;
                }
                objectMoleculesAsList.addAll(Arrays.asList(v2o.get(i).getMolecules()));
            }

            objectMolecules = objectMoleculesAsList.toArray(new String[objectMoleculesAsList.size()]);

            // compare
            for (String s1 : objectMolecules) {
                for (String s2 : verb1Molecules) {
                    if (s1.equals(s2)) {
                        needToKeepObject = true;
                    }
                }
            }

            if (!needToKeepObject) {
                System.out.println("no need to keep object");
                frames[0].setObject("");
                part0List.addAll(s);
                part0List.add(sentenceAsList.get(verb1Index));
            } else {
                System.out.println("need to keep object");
                part0List.addAll(s);
                part0List.add(sentenceAsList.get(verb1Index));
                // add object's def nodes
                for (int i = 0; i < v2o.size(); i++) {
                    if (i == 0) {
                        continue;
                    }
                    part0List.add(v2o.get(i));
                }
            }

            part0 = part0List.toArray(new WordDefinitionNode[part0List.size()]);

            // populate
            populateMemory(frames[0], part0);
            populateMemory(frames[1], part1);

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Failed to apply processANDcase5.");
        }
    }

    /**
     * Use frame and associated <code>WordDefinitionNode</code>s to populate
     * memory
     *
     * @param frame
     * @param nodes
     */
    private void populateMemory(SVOFrame frame, WordDefinitionNode[] nodes) {
        ArrayList<String> molecules = new ArrayList<>();
        List<WordDefinitionNode> subjectNodes = new ArrayList<>();
        List<WordDefinitionNode> objectNodes = new ArrayList<>();

        int subIndex = frame.getSubject().split(" ").length;
        boolean addMolecule = true;

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].getTrigger().equals(frame.getVerb())) {
                addMolecule = false;
            }

            if (addMolecule) {
                String[] wordMolecules = nodes[i].getMolecules();

                if (wordMolecules.length != 0) {
                    molecules.addAll(Arrays.asList(wordMolecules));
                }
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
            if (!frame.getObject().isEmpty()) {
                memory.addStateNode(frame.getObject());
            }
        } else {
            memory.addConceptNode(frame.getSubject(), ConceptType.EVENT, molecules);
            memory.addEventNode(frame.getVerb(), frame.getObject());
        }

        // definition nodes for subject
        String primeDefSubject = generatePrimeRepForText(subjectNodes.toArray(new WordDefinitionNode[subjectNodes.size()]));

        if (memory.hasDefinitionNode(frame.getSubject(), primeDefSubject)) {
            memory.addDefinitionNode(frame.getSubject(), primeDefSubject, POS.S);
        }

        // definition nodes for object
        if (!frame.getObject().isEmpty()) {
            String primeDefObject = generatePrimeRepForText(objectNodes.toArray(new WordDefinitionNode[objectNodes.size()]));

            if (memory.hasDefinitionNode(frame.getObject(), primeDefObject)) {
                memory.addDefinitionNode(frame.getObject(), primeDefObject, POS.O);
            }
        }
        System.out.println("Populated memory with " + frame.toString());
    }
}
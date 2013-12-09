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

import java.util.ArrayList;
import memory.Memory;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;

/**
 * AParser class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 7:23:40 PM,
 * Nov 28, 2013 Description:
 */
public abstract class AParser {

    public final Memory memory;

    public AParser(Memory memory) {
        this.memory = memory;
    }

    protected WordDefinitionNode[][] tokenizeSentence(String sentence) {
        String[] words = sentence.split(" ");
        ArrayList<ArrayList<WordDefinitionNode>> masterList = new ArrayList<>();

        // get definition nodes for each word
        int noOfCombos = 1;
        for (String word : words) {
            masterList.add((ArrayList<WordDefinitionNode>) memory.getWordNodes(word));
            noOfCombos *= masterList.get(masterList.size() - 1).size();
        }

        // get all possible combinations
        WordDefinitionNode[][] combinations = new WordDefinitionNode[noOfCombos][words.length];

        for (int i = 0; i < words.length; i++) {
            int count = 0, changeAt = noOfCombos / masterList.get(i).size();
            for (int j = 0; j < noOfCombos; j++) {
                if (j != 0 && j % changeAt == 0) {
                    count++;
                }
                combinations[j][i] = masterList.get(i).get(count);
            }
        }

        // TODO: remove duplicates
        return combinations;
    }

    protected String generatePrimeRepForText(String[] text, WordDefinitionNode[] nodes) {
        String primeRep = "";

        boolean the = false, in = false;
        for (int i = 0; i < text.length; i++) {
            String s = text[i];
            if (s.equals("")) {
                //continue;
                System.err.println("Error: Found empty string when generating prime rep for text: ");
                for (String ts: text) {
                    System.err.print(ts + " ");
                }
                System.err.println();
                return "";
            }
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
                if (((WordDefinitionNode) (nodes[i])).getPos().equals(POS.N)
                        || (text[text.length - 1] == s)) {
                    primeRep += nodes[i].getPrimeRepresentation() + ")$$";
                    the = false;
                } else {
                    primeRep += nodes[i].getPrimeRepresentation() + ",";
                }
            } else if (in) {
                if (((WordDefinitionNode) (nodes[i])).getPos().equals(POS.N)
                        || (text[text.length - 1] == s)) {
                    primeRep += nodes[i].getPrimeRepresentation() + ")$$";
                    in = false;
                } else {
                    primeRep += nodes[i].getPrimeRepresentation() + ",";
                }
            } else {
                primeRep += nodes[i].getPrimeRepresentation() + "$$";
            }
        }
        return primeRep;
    }

    protected boolean isState(String verb) {
        return (verb.equals("are") || verb.equals("is") || verb.equals("was") || verb.equals("was"));
    }

    protected String cleanString(String text) {
        return text.replace("?", "");
    }
}

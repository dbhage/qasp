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
package query.cma;

import frame.AFrame;
import frame.SVOFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import memory.Memory;
import memory.Prime;
import memory.node.ConceptNode;
import memory.node.EventNode;
import memory.node.definition.DefinitionNode;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;
import query.QueryResult;

/**
 * CMA1 class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:16:29 PM
 * Description:
 */
public class CMA1 implements ICMA {

    private Memory memory;
    private final int O_THRESHOLD = 1;
    private final int V_THRESHOLD = 1;

    @Override
    public QueryResult search(AFrame frame, Memory memory) {
        this.memory = memory;
        String[] res = search(frame);

        if (res == null) {
            return new QueryResult("no answer");
        }

        String result = "";

        for (String s : res) {
            result += s + " ";
        }

        QueryResult queryResult = new QueryResult(result);
        return queryResult;
    }

    private void printLn(String s) {
        System.out.println(s);
    }

    private String stringArrayToString(String[] s) {
        String s1 = "";
        for (String s2 : s) {
            s1 += s2 + ",";
        }
        return s1;
    }

    private String[] search(AFrame frame) {
        printLn("------------------------------------------------------");

        // set up ----------------------------------------------------------------------
        SVOFrame svoFrame = (SVOFrame) frame;
        printLn("Start search with input -> " + svoFrame.toString());

        // get molecules and elements
        String[] sMolecules = getMolecules(svoFrame.getSubject());
        printLn("Subject Molecules: " + stringArrayToString(sMolecules));

        String[] vMolecules = memory.getDefinitionNode(svoFrame.getVerb()).getMolecules();
        printLn("Verb Molecules: " + stringArrayToString(vMolecules));

        String[] oElements = svoFrame.getObject().split(" ");
        printLn("Object Elements: " + stringArrayToString(oElements));

        // get concept lists w.r.t. elements in sMolecules
        // we need 2 lists, one containing all of question's subject's molecules
        // and one containing atleast 1 molecules from question's subject.
        ArrayList<ConceptNode> containsEveryElement = new ArrayList<>();
        ArrayList<ConceptNode> containsAtLeastOneElement = new ArrayList<>();
        boolean atleast, all;

        for (ConceptNode conceptNode : memory.getAllConcepts()) {
            atleast = false;
            all = true;
            for (String molecules : sMolecules) {
                if (conceptNode.containsMolecule(molecules)) {
                    if (!atleast) {
                        containsAtLeastOneElement.add(conceptNode);
                        atleast = true;
                    }
                } else {
                    all = false;
                }
            }
            if (all) {
                containsEveryElement.add(conceptNode);
                containsAtLeastOneElement.remove(conceptNode);
            }
        }

        printLn("Concepts containing all molecules from sMolecules: " + containsEveryElement.size());
        printLn("Concepts containing atleast one (but not all) molecule from sMolecules: " + containsAtLeastOneElement.size());

        // concepts containing ALL molecules ---------------------------------
        int score;
        HashMap<Integer, String> verbScoreMap = new HashMap<>();
        ArrayList<Integer> verbScores = new ArrayList<>();

        // for all concepts contain ALL semantic molecules from frame.getSubject()
        for (ConceptNode conceptNode : containsEveryElement) {
            score = 0;
            // get event
            EventNode event = memory.getEvent(conceptNode.getTypeId());
            if (event == null) {
                throw new NullPointerException("Concept does not have an associated event.");
            }

            // get verb's moelcules
            String[] verbMolecules = memory.getDefinitionNode(event.getVerb()).getMolecules();

            printLn("Event #" + event.getId() + "'s verb \""
                    + event.getVerb() + "\"'s molecules: "
                    + stringArrayToString(verbMolecules));

            boolean allVerbs = true;

            for (String verbMolecule : verbMolecules) {
                boolean found = false;
                for (String vMolecule : vMolecules) {
                    if (vMolecule.equals(verbMolecule)) {
                        found = true;
                        score++;
                    }
                }
                if (!found) {
                    allVerbs = false;
                }
            }

            // if v contains all molecules from vMolecules
            if (allVerbs) {
                String[] curOElements = event.getObject().split(" ");
                if (closeEnough(curOElements, oElements, O_THRESHOLD)) {
                    return curOElements;
                }
            } else {
                if (score >= V_THRESHOLD) {
                    verbScoreMap.put(score, event.getObject());
                    verbScores.add(score);
                }
            }
        }

        // go over verbs in a descending order based on score
        Collections.sort(verbScores);

        for (int i : verbScores) {
            String[] o = verbScoreMap.get(i).split(" ");
            if (closeEnough(o, oElements, O_THRESHOLD)) {
                return o;
            }
        }

        // concepts with atleast 1 molecule ----------------------------------------------
        // for all concepts contain ATLEAST ONE semantic molecules from frame.getSubject()        
        verbScores.clear();
        verbScoreMap.clear();

        // score the concepts
        HashMap<Integer, ConceptNode> conceptScoreMap = new HashMap<>();
        ArrayList<Integer> conceptScores = new ArrayList<>();

        for (ConceptNode conceptNode : containsAtLeastOneElement) {
            score = 0;
            ArrayList<String> currentConceptMolecules = (ArrayList<String>) conceptNode.getMolecules();
            for (String currentConceptMolecule : currentConceptMolecules) {
                for (String sMolecule : sMolecules) {
                    if (currentConceptMolecule.equals(sMolecule)) {
                        ++score;
                    }
                }
            }
            conceptScoreMap.put(score, conceptNode);
            conceptScores.add(score);
        }

        Collections.sort(conceptScores);

        for (int i : conceptScores) {
            ConceptNode conceptNode = conceptScoreMap.get(i);
            score = 0;
            // get event
            EventNode event = memory.getEvent(conceptNode.getTypeId());
            if (event == null) {
                throw new NullPointerException("Concept does not have an associated event.");
            }

            // get verb's moelcules
            String[] verbMolecules = memory.getDefinitionNode(event.getVerb()).getMolecules();
            boolean allVerbs = true;

            for (String verbMolecule : verbMolecules) {
                boolean found = false;
                for (String vMolecule : vMolecules) {
                    if (vMolecule.equals(verbMolecule)) {
                        found = true;
                        score++;
                    }
                }
                if (!found) {
                    allVerbs = false;
                }
            }

            // if v contains all molecules from vMolecules
            if (allVerbs) {
                String[] curOElements = event.getObject().split(" ");
                if (closeEnough(curOElements, oElements, O_THRESHOLD)) {
                    return curOElements;
                }
            } else {
                if (score >= V_THRESHOLD) {
                    verbScoreMap.put(score, event.getObject());
                    verbScores.add(score);
                }
            }
        }

        // go over verbs in a descending order based on score
        Collections.sort(verbScores);

        for (int i : verbScores) {
            String[] o = verbScoreMap.get(i).split(" ");
            if (closeEnough(o, oElements, O_THRESHOLD)) {
                return o;
            }
        }

        printLn("------------------------------------------------------");

        // check regardless of molecules
        // search continues based on primes
        // eg: try "joseph ate a burger", "what did joseph eat?"
        // no common molecules in subject but prime PEOPLE is 
        // common in the subjects.
        return null;
    }

    private String[] getMolecules(String text) {
        ArrayList<String> moleculeList = new ArrayList<>();
        for (String word : text.split(" ")) {
            moleculeList.addAll(Arrays.asList(memory.getDefinitionNode(word).getMolecules()));
        }
        return moleculeList.toArray(new String[moleculeList.size()]);
    }

    private boolean closeEnough(String[] curOElements, String[] oElements, int oThreshold) {
        int score = 0;

        printLn("CloseEnough: \n"
                + "\tcurOElements: " + stringArrayToString(curOElements)
                + "\n\toElements: " + stringArrayToString(oElements)
                + "\n\toThreshold: " + oThreshold);

        Set<String> questionPrimeCategories = new TreeSet<>();

        for (String s : oElements) {
            WordDefinitionNode word = (WordDefinitionNode) (memory.getDefinitionNode(s));
            if (word.getPos().equals(POS.Q)) {
                questionPrimeCategories.addAll(Arrays.asList(word.getPrimeRepresentation().split(",")));
            }
        }

        Set<String> objectPrimes = new TreeSet<>();

        for (String s : curOElements) {
            if (s.isEmpty()) {
                continue;
            }
            DefinitionNode defNode = memory.getDefinitionNode(s);
            String primeRepresentation = defNode.getPrimeRepresentation();
            String[] currPrimes = primeRepresentation.split("[^A-Z]");
            for (String currPrime : currPrimes) {
                if (!currPrime.isEmpty()) {
                    objectPrimes.add(currPrime);
                }
            }
        }

        for (Prime prime : Memory.primes) {
            for (String objectPrime : objectPrimes) {
                if (prime.getPrime().equals(objectPrime)) {
                    for (String questionPrimeCategory : questionPrimeCategories) {
                        if (questionPrimeCategory.equals(prime.getCategory())) {
                            ++score;
                            if (score >= oThreshold) {
                                System.out.println("Score: " + score);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}

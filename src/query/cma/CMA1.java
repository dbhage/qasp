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
import java.util.List;
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
    private final int V_THRESHOLD = 1;
    private List<QueryResult> queryResults;

    @Override
    public List<QueryResult> search(AFrame frame, Memory memory) {
        this.memory = memory;
        this.queryResults = new ArrayList<>();
        search(frame);

        if (queryResults.isEmpty()) {
            return null;
        }

        Collections.sort(queryResults);

        int startIndex = queryResults.size() - 1,
                highestScore = queryResults.get(queryResults.size() - 1).getQueryScore();

        for (int i = queryResults.size() - 2; i >= 0; i--) {
            if (queryResults.get(i).getQueryScore() == highestScore) {
                startIndex = i;
            }
        }

        return queryResults.subList(startIndex, queryResults.size());
    }

    private String stringArrayToString(String[] s) {
        String s1 = "";
        for (String s2 : s) {
            s1 += s2 + ",";
        }
        if (s1.endsWith(","))
            s1 = s1.substring(0, s1.length() - 1);
        return s1;
    }

    private void search(AFrame frame) throws NullPointerException {
        //<editor-fold  defaultstate="collapsed" desc="set up">
        System.out.println("-----------------------------");

        // set up ----------------------------------------------------------------------
        SVOFrame svoFrame = (SVOFrame) frame;
        System.out.println("Start search with input: " + svoFrame.toString());

        // get molecules and elements
        String[] sMolecules = getMolecules(svoFrame.getSubject());
        System.out.println("Subject Molecules: " + stringArrayToString(sMolecules));

        String[] vMolecules = memory.getDefinitionNode(svoFrame.getVerb()).getMolecules();
        System.out.println("Verb Molecules: " + stringArrayToString(vMolecules));

        String[] oElements = svoFrame.getObject().split(" ");
        System.out.println("Object Elements: " + stringArrayToString(oElements));
        //</editor-fold>        

        //<editor-fold defaultstate="collapsed" desc="sMolecules not null">
        if (sMolecules.length > 0) {
            // get concept lists w.r.t. elements in sMolecules
            // we need 2 lists, one containing all of question's subject's molecules
            // and one containing atleast 1 molecules from question's subject.
            List<ConceptNode> containsEveryElement = new ArrayList<>();
            List<ConceptNode> containsAtLeastOneElement = new ArrayList<>();
            List<CloseEnoughClass> closeEnoughs = new ArrayList<>();
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

            System.out.println("Concepts containing all molecules from sMolecules: " + containsEveryElement.size());

            // concepts containing ALL molecules ---------------------------------
            int score;

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

//                System.out.println("Event #" + event.getId() + "'s verb \""
//                        + event.getVerb() + "\"'s molecules: "
//                        + stringArrayToString(verbMolecules));
//
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
                    closeEnough(curOElements, oElements, conceptNode.getText(), svoFrame.getSubject());
                } else {
                    if (score >= V_THRESHOLD) {
                        closeEnoughs.add(new CloseEnoughClass(score, event.getObject(), conceptNode.getText()));
                    }
                }
            }

            if (!queryResults.isEmpty()) {
                return; // already found answers and won't find anything better, so return
            }
            // go over verbs in a descending order based on highestScore
            Collections.sort(closeEnoughs);

            for (CloseEnoughClass cE : closeEnoughs) {
                closeEnough(cE.eventObject.split(" "), oElements, cE.conceptText, svoFrame.getSubject());
                if (!queryResults.isEmpty()) {
                    return;
                }
            }

            // concepts with atleast 1 molecule ----------------------------------------------
            // for all concepts contain ATLEAST ONE semantic molecules from frame.getSubject()        
            System.out.println("Concepts containing atleast one (but not all) molecule from sMolecules: " + containsAtLeastOneElement.size());

            closeEnoughs.clear();

            // highestScore the concepts
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

            Collections.sort(conceptScores); // need to traverse this from end

            for (int j = conceptScores.size() - 1; j >= 0; j--) {
                int i = conceptScores.get(j);
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
                    closeEnough(curOElements, oElements, conceptNode.getText(), svoFrame.getSubject());
                    if (!queryResults.isEmpty()) {
                        return;
                    }
                } else {
                    if (score >= V_THRESHOLD) {
                        closeEnoughs.add(new CloseEnoughClass(score, event.getObject(), conceptNode.getText()));
                    }
                }
            }

            // go over verbs in a descending order based on highestScore
            Collections.sort(closeEnoughs);

            for (CloseEnoughClass cE : closeEnoughs) {
                closeEnough(cE.eventObject.split(" "), oElements, cE.conceptText, svoFrame.getSubject());
                if (!queryResults.isEmpty()) {
                    return;
                }
            }
        }
        
        //</editor-fold>

        
        
        //<editor-fold defaultstate="collapsed" desc="perform closest match using primes only">
        // check regardless of molecules
        // search continues based on primes
        // eg: try "joseph ate a burger", "what did joseph eat?"
        // no common molecules in subject but prime PEOPLE is 
        // common in the subjects.
        
        
        
        //</editor-fold>
        System.out.println("-----------------------------");
    }

    private String[] getMolecules(String text) {
        System.out.print("Text \"" + text + "\" has molecules: ");
        ArrayList<String> moleculeList = new ArrayList<>();
        for (String word : text.split(" ")) {
            moleculeList.addAll(Arrays.asList(memory.getDefinitionNode(word).getMolecules()));
        }
        String[] moleculesArray = moleculeList.toArray(new String[moleculeList.size()]);
        System.out.println(stringArrayToString(moleculesArray));
        return moleculesArray;
    }

    private void closeEnough(String[] curOElements, String[] oElements, String conceptSubject, String questionSubject) {
        //<editor-fold defaultstate="collapsed" desc=" compare prime categories">
        int noOfPrimeCategoriesInCommon = 0;

        System.out.println("CloseEnough: \n"
                + "\tcurOElements: " + stringArrayToString(curOElements)
                + "\n\toElements: " + stringArrayToString(oElements));

        Set<String> questionPrimeCategories = new TreeSet<>();

        for (String s : oElements) {
            if (s.equals("what")) {
                noOfPrimeCategoriesInCommon++;
            }
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
                            ++noOfPrimeCategoriesInCommon;
                        }
                    }
                }
            }
        }
        //</editor-fold>

        if (noOfPrimeCategoriesInCommon > 0) {
            // compare # primes in subject for question and concept's subject.
            // might need to expand molecules but not for now

            int score = 0;
            String[] sub1 = conceptSubject.split(" ");
            String[] sub2 = questionSubject.split(" ");

            for (String s1 : sub1) {
                for (String s2 : sub2) {
                    if (s1.equals(s2)) {
                        score++;
                    }
                }
            }

            String primeRepForSub1 = "";
            for (String s1 : sub1) {
                primeRepForSub1 += memory.getDefinitionNode(s1).getPrimeRepresentation() + ",";
            }
            String[] sub1Primes = primeRepForSub1.split(",");

            String primeRepForSub2 = "";
            for (String s2 : sub2) {
                primeRepForSub2 += memory.getDefinitionNode(s2).getPrimeRepresentation() + ",";
            }
            String[] sub2Primes = primeRepForSub2.split(",");

            for (String sub1Prime : sub1Primes) {
                if (sub1Prime.startsWith("#")) {
                    continue;
                }
                for (String sub2Prime : sub2Primes) {
                    if (sub2Prime.startsWith("#")) {
                        continue;
                    }
                    if (sub1Prime.equals(sub2Prime)) {
                        score++;
                    }
                }
            }

            queryResults.add(new QueryResult(stringArrayToString(curOElements).replace(',', ' '), score));
            System.out.println("Found: " + queryResults.get(queryResults.size() - 1).toString());
        }
    }

    /**
     * When sorted, this will sort in descending order of
     * noOfPrimeCategoriesInCommon
     */
    private class CloseEnoughClass implements Comparable<CloseEnoughClass> {

        public String eventObject;
        public String conceptText;
        public int score;

        /**
         * Constructor
         *
         * @param s - noOfPrimeCategoriesInCommon
         * @param eO - event's object
         * @param cT - concept's text
         */
        public CloseEnoughClass(int s, String eO, String cT) {
            this.score = s;
            this.eventObject = eO;
            this.conceptText = cT;
        }

        @Override
        public int compareTo(CloseEnoughClass o) {
            if (this.score == o.score) {
                return 0;
            } else if (this.score < o.score) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}

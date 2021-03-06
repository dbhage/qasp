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

package memory.node.definition;

import java.util.ArrayList;
import java.util.regex.*;

/**
 * DefinitionNode class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 1:26:31 PM, Nov 7, 2013 
 * Description: data structure to represent a definition node
 */
public class DefinitionNode {

    private final String trigger;
    private final String primeRepresentation;
    private final String[] molecules;

    /**
     * Constructor
     * @param trigger - trigger for node
     * @param primeRep - prime representation
     */
    public DefinitionNode(String trigger, String primeRep) {
        this.trigger = trigger;
        this.primeRepresentation = primeRep;

        ArrayList<String> moleculeList = new ArrayList<>();
        Pattern pattern = Pattern.compile("#[a-z]+");
        Matcher matcher = pattern.matcher(primeRep);
        while (matcher.find()) {
            moleculeList.add(matcher.group());
        }
        this.molecules = moleculeList.toArray(new String[moleculeList.size()]);
    }

    /**
     * Check if <code>DefinitionNode</code> is a molecule
     * @return true if node is a molecule, false otherwise
     */
    public boolean isMolecule() {
        return trigger.startsWith("#");
    }

    /**
     * @return the trigger
     */
    public String getTrigger() {
        return trigger;
    }

    /**
     * @return the primeRepresentation
     */
    public String getPrimeRepresentation() {
        return primeRepresentation;
    }

    /**
     * @return the molecules
     */
    public String[] getMolecules() {
        return molecules;
    }
}
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

/**
 * WordDefinitionNode class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 1:26:31 PM, Nov 7, 2013 
 * Description: data structure to represent a word definition node
 */
public class WordDefinitionNode extends DefinitionNode {

    /** part of speech tag */
    private final POS pos;

    /**
     * Constructor
     * @param trigger - node's trigger
     * @param primeRep - prime representation
     * @param pos  - part of speech enum
     */
    public WordDefinitionNode(String trigger, String primeRep, POS pos) {
        super(trigger, primeRep);
        if (pos == null) {
            throw new NullPointerException("Creating word \"" + trigger + "\" a null POS");
        }
        this.pos = pos;
    }

    /**
     * @return the pos
     */
    public POS getPos() {
        return pos;
    }

    /**
     * Check if node is a verb
     * @return true if verb, false otherwise
     */
    public boolean isVerb() {
        return pos.equals(POS.V);
    }
}
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
package memory.node;

import java.util.ArrayList;
import java.util.List;

/**
 * ConceptNode class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:39:08 PM,
 * Nov 7, 2013 Description:
 */
public class ConceptNode {

    private final String text;
    private final int typeId;
    private final List<String> molecules;
    private final ConceptType type;

    public ConceptNode(String text, ConceptType type, int tid, ArrayList<String> molecules) {
        this.text = text;
        this.typeId = tid;
        this.molecules = molecules;
        this.type = type;
    }

    public void addMolecule(String molecule) {
        if (molecule != null) {
            molecules.add(molecule);
        }
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the typeId
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * @return the molecules
     */
    public List<String> getMolecules() {
        return molecules;
    }

    /**
     * @return the type
     */
    public ConceptType getType() {
        return type;
    }

    public boolean containsMolecule(String molecule) {
        for (String mol : molecules) {
            if (mol.equals(molecule)) {
                return true;
            }
        }
        return false;
    }
}

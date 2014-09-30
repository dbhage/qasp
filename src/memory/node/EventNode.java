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

/**
 * EventNode class.
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 1:24:30 PM, Nov 7, 2013 
 * Description: data structure to represent an event node
 */
public class EventNode {

    /** id */
    private final int id;
    
    /** verb */
    private final String verb;
    
    /** object */
    private final String object;

    /**
     * Constructor
     * @param n - event number
     * @param v - event verb
     * @param o - event object
     */
    public EventNode(int n, String v, String o) {
        this.id = n;
        this.verb = v;
        this.object = o;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the verb
     */
    public String getVerb() {
        return verb;
    }

    /**
     * @return the object
     */
    public String getObject() {
        return object;
    }
}
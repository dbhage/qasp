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

package frame;

/**
 * SVOFrame - Subject Verb Object Frame
 * @author Dwijesh Bhageerutty, neerav789@gmail.com 
 * Date created: 1:40:20 PM, Nov 7, 2013 
 * Description: Data structure to store an SVO frame
 */

public class SVOFrame extends AFrame {

    /**
     * The subject of the sentence
     */
    private String subject;

    /**
     * The verb of the sentence
     */
    private String verb;

    /**
     * The object of the sentence
     */
    private String object;

    public SVOFrame() {
    }
    
    public SVOFrame(String s, String v, String o) {
        this.subject = s;
        this.verb = v;
        this.object = o;
    }
    
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
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

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @param verb the verb to set
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }

    /**
     * @param object the object to set
     */
    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "S: " + subject + " V: " + verb + " O: " + object;
    }
    
    @Override
    public SVOFrame clone() {
        return new SVOFrame(this.subject, this.verb, this.object);
    }
}
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
import memory.Memory;
import memory.node.definition.POS;
import memory.node.definition.WordDefinitionNode;

/**
 * QueryParser class.
 *
 * @author Dwijesh Bhageerutty, neerav789@gmail.com Date created: 1:04:39 PM,
 * Nov 7, 2013 Description:
 */
public class QueryParser extends AParser implements IParser {

    public AFrame[] frames;

    public QueryParser(Memory memory) {
        super(memory);
    }

    @Override
    public void parse(String text) {
        text = cleanString(text);
        WordDefinitionNode[][] tokenizedSentences = tokenizeSentence(text);
        frames = new AFrame[tokenizedSentences.length];
        for (int i = 0; i < tokenizedSentences.length; i++) {
            frames[i] = processSimpleQuery(text, tokenizedSentences[i]);
        }
    }

    private AFrame processSimpleQuery(String text, WordDefinitionNode[] words) {
        AFrame frame = new SVOFrame();
        for (WordDefinitionNode word : words) {
            if (word.getPos().equals(POS.Det) && ((SVOFrame) frame).getObject() == null) {
                ((SVOFrame) frame).setObject(text.split(word.getTrigger())[0]);
            }
            if (word.getPos().equals(POS.V) && words[words.length - 1].equals(word)) {
                ((SVOFrame) frame).setVerb(word.getTrigger());
                String sub = (text.replaceAll(word.getTrigger(), ""));
                sub = sub.replaceAll(((SVOFrame) frame).getObject(), "");
                ((SVOFrame) frame).setSubject(sub);
            }
        }
        return frame;
    }

    public AFrame[] getFrames() {
        return frames;
    }
}
